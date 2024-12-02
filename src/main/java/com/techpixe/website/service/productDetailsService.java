package com.techpixe.website.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.techpixe.website.entity.ProductDetails;
import com.techpixe.website.entity.ProductFeature;

public interface productDetailsService {

	ProductDetails createProductDetails(Long productId, String name, String description, List<String> features,
			List<MultipartFile> images, List<String> imageDescriptions, List<MultipartFile> videos, List<String> videoDescriptions) throws IOException;


	void deleteProductDetails(Long id);


	ProductDetails getProductDetailsById(Long productDetailsId);


	ProductDetails getProductDetailsByProductId(Long productId);


	ProductDetails updateProductDetails(ProductDetails existingProductDetails, String name, String description,
			List<Map<String, Object>> features, List<Map<String, Object>> mediaList) throws IOException;






}
