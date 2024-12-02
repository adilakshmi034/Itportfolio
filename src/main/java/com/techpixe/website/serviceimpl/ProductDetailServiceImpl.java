package com.techpixe.website.serviceimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techpixe.website.entity.Product;
import com.techpixe.website.entity.ProductDetails;
import com.techpixe.website.entity.ProductFeature;
import com.techpixe.website.entity.ProductImage;
import com.techpixe.website.repository.ProductRepository;
import com.techpixe.website.repository.productDetailsRepository;
import com.techpixe.website.service.productDetailsService;

import jakarta.transaction.Transactional;

@Service
public class ProductDetailServiceImpl implements productDetailsService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private productDetailsRepository productDetailRepository;

	@Override
	public ProductDetails createProductDetails(Long productId, String name, String description,
			List<String> featureDescriptions, List<MultipartFile> images, List<String> imageDescriptions,
			List<MultipartFile> videos, List<String> videoDescriptions) throws IOException {

		// Fetch product by ID
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

		// Initialize ProductDetails
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setName(name);
		productDetails.setDescription(description);

		// Add features to ProductDetails
		List<ProductFeature> features = new ArrayList<>();
		if (featureDescriptions != null) {
			for (String featureDescription : featureDescriptions) {
				ProductFeature feature = new ProductFeature();
				feature.setDescription(featureDescription);
				feature.setProductDetails(productDetails);
				features.add(feature);
			}
		}
		productDetails.setFeatures(features);

		// Add images and videos to ProductDetails
		List<ProductImage> productImages = new ArrayList<>();
		if (images != null && imageDescriptions != null) {
			for (int i = 0; i < images.size(); i++) {
				MultipartFile imageFile = images.get(i);
				String imageDescription = i < imageDescriptions.size() ? imageDescriptions.get(i) : "";

				ProductImage productImage = new ProductImage();
				productImage.setImageData(imageFile.getBytes());
				productImage.setImageDescription(imageDescription);
				productImage.setProductDetails(productDetails);
				productImages.add(productImage);
			}
		}
		if (videos != null && videoDescriptions != null) {
			for (int i = 0; i < videos.size(); i++) {
				MultipartFile videoFile = videos.get(i);
				String videoDescription = i < videoDescriptions.size() ? videoDescriptions.get(i) : "";

				ProductImage productImage = new ProductImage();
				productImage.setVideoData(videoFile.getBytes());
				productImage.setVideoDescription(videoDescription);
				productImage.setProductDetails(productDetails);
				productImages.add(productImage);
			}
		}
		productDetails.setImages(productImages);

		return productDetailRepository.save(productDetails);
	}

	@Override
	public ProductDetails updateProductDetails(ProductDetails existingProductDetails, String name, String description,
			List<Map<String, Object>> features, List<Map<String, Object>> mediaList) throws IOException {

		// Update basic details
		// System.err.println(features+"features");
		existingProductDetails.setName(name);
		existingProductDetails.setDescription(description);

		// System.err.print(existingProductDetails.getFeatures().get(0)+"102");

		// Update or add features
		updateOrAddFeatures(existingProductDetails, features);

		updateOrAddMedia(existingProductDetails, mediaList);

		// Save and return the updated ProductDetails
		return productDetailRepository.save(existingProductDetails);
	}

	
	private void updateOrAddFeatures(ProductDetails existingProductDetails, List<Map<String, Object>> features) {
	    // Iterate over the incoming features list
	    for (Map<String, Object> featureData : features) {
	        Long featureId = featureData.containsKey("featureId") ? Long.valueOf(featureData.get("featureId").toString()) : null;
	        String description = featureData.containsKey("description") ? featureData.get("description").toString() : null;

	        if (featureId != null) {
	            // Find existing feature by featureId
	            boolean featureUpdated = false;
	            for (ProductFeature existingFeature : existingProductDetails.getFeatures()) {
	                if (existingFeature.getFeatureId().equals(featureId)) {
	                    // Update the description
	                    existingFeature.setDescription(description);
	                    featureUpdated = true;
	                    break;
	                }
	            }

	            if (!featureUpdated) {
	                // If the featureId does not exist in the existing list, log or handle it as required
	                System.out.println("Feature with ID " + featureId + " not found for update.");
	            }
	        } else {
	            // Add a new feature if featureId is not provided
	            ProductFeature newFeature = new ProductFeature();
	            newFeature.setDescription(description);
	            newFeature.setProductDetails(existingProductDetails);
	            existingProductDetails.getFeatures().add(newFeature);
	        }
	    }
	}

	private void updateOrAddMedia(ProductDetails existingProductDetails, List<Map<String, Object>> mediaList) {
	    if (mediaList != null) {
	    	
	    	//System.err.println(mediaList+"mediaList");
	        for (Map<String, Object> mediaData : mediaList) {
	            Long imageId = mediaData.get("imageId") != null ? Long.valueOf(mediaData.get("imageId").toString()) : null;

	            // Determine type (image or video) and descriptions
	            String imageDataStr = (String) mediaData.get("imageData");
	            String videoDataStr = (String) mediaData.get("videoData");
	            String imageDescription = (String) mediaData.get("imageDescription");
	            String videoDescription = (String) mediaData.get("videoDescription");

	            ProductImage media = null;

	            if (imageId != null) {
	           
	                media = existingProductDetails.getImages().stream()
	                        .filter(img -> img.getImageId().equals(imageId))
	                        .findFirst()
	                        .orElse(null);
	            }

	            if (media != null) {
	                // Update existing media
	                if (imageDataStr != null) {
	                    media.setImageData(Base64.getDecoder().decode(imageDataStr));
	                }
	                if (videoDataStr != null) {
	                    media.setVideoData(Base64.getDecoder().decode(videoDataStr));
	                }
	                media.setImageDescription(imageDescription);
	                media.setVideoDescription(videoDescription);
	                
	                existingProductDetails.getImages().add(media);
	            }
	            
//	            else {
//	                media = new ProductImage();
//	                if (imageDataStr != null) {
//	                    media.setImageData(Base64.getDecoder().decode(imageDataStr));
//	                }
//	                if (videoDataStr != null) {
//	                    media.setVideoData(Base64.getDecoder().decode(videoDataStr));
//	                }
//	                media.setImageDescription(imageDescription);
//	                media.setVideoDescription(videoDescription);
//	                media.setProductDetails(existingProductDetails);
//	                existingProductDetails.getImages().add(media);
//	            }
	            
	            
	        }
	    }
	}
	
	@Transactional
	public void deleteProductDetails(Long productDetaiId) {
		productDetailRepository.deleteProductImagesByProductDetailId(productDetaiId);
		productDetailRepository.deleteProductFeaturesByProductDetailId(productDetaiId); 
		productDetailRepository.deleteProductDetailsById(productDetaiId); // Delete parent
	}

	@Override
	public ProductDetails getProductDetailsById(Long productDetailsId) {
		System.err.println(productDetailsId + "productDetailsId");
		return productDetailRepository.findById(productDetailsId)
				.orElseThrow(() -> new RuntimeException("ProductDetails not found with ID: " + productDetailsId));
	}

	public ProductDetails getProductDetailsByProductId(Long productId) {
		return productDetailRepository.findByProductId(productId);
	}
}
