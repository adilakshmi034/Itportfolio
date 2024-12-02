package com.techpixe.website.serviceimpl;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techpixe.website.dto.ProductDto;
import com.techpixe.website.entity.Product;
import com.techpixe.website.entity.SubCategory;
import com.techpixe.website.repository.ProductRepository;
import com.techpixe.website.repository.SubCategoryRepository;
import com.techpixe.website.service.ProductService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

//	@Autowired
//	private SuperAdminRepository superAdminRepository;

	@Autowired
	private SubCategoryRepository subcategoryRepository;

//	@Autowired
//	private RoleRepository roleRepository;

	public Product createProduct(ProductDto productDto, Long subCategoryId) {
		// Fetch subcategory by ID
		SubCategory subCategory = subcategoryRepository.findById(subCategoryId)
				.orElseThrow(() -> new EntityNotFoundException("SubCategory not found"));

		// Create new product entity
		Product product = new Product();
		product.setProductName(productDto.getProductName());
		product.setProductDescription(productDto.getProductDescription());
		product.setPrice(productDto.getPrice());
		product.setDiscountPrice(productDto.getDiscountPrice());
		product.setRating(productDto.getRating());
		product.setImage(productDto.getImage());
		product.setCreatedAt(LocalDate.now());

		// Calculate offer price based on percentage
		double discountAmount = productDto.getPrice() * (productDto.getDiscountPrice() / 100);
		double offerPrice = Math.round(productDto.getPrice() - discountAmount);

		if (offerPrice < 0) {
			throw new IllegalArgumentException("Invalid discount percentage: Offer price cannot be negative");
		}

		product.setOfferPrice(offerPrice);

		product.setSubcategory(subCategory); // Set the subcategory

		// Save and return the created product
		return productRepository.save(product);
	}

	public List<Product> getProductsByCategory(Long subCategoryId) {
		return productRepository.findBySubCategoryId(subCategoryId);
	}

	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product updateProduct(Long productId, String productName, String productDescription, Double price,
			Double discountPrice, Long rating, byte[] image) {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();

// Update product details if provided
			if (productName != null)
				product.setProductName(productName);
			if (productDescription != null)
				product.setProductDescription(productDescription);
			if (price != null)
				product.setPrice(price);
			if (discountPrice != null) {
				product.setDiscountPrice(discountPrice);
			}
			if (rating != null)
				product.setRating(rating);
			if (image != null)
				product.setImage(image);

// Recalculate offer price if price or discountPercentage has changed
			if (price != null || discountPrice != null) {
				double updatedPrice = price != null ? price : product.getPrice();
				double updatedDiscountPercentage = discountPrice != null ? discountPrice
						: product.getDiscountPrice();

				double discountAmount = updatedPrice * (updatedDiscountPercentage / 100);
				double offerPrice =Math.round(updatedPrice - discountAmount);

				if (offerPrice < 0) {
					throw new IllegalArgumentException("Invalid discount percentage: Offer price cannot be negative");
				}

				product.setOfferPrice(offerPrice);
			}

// Save updated product and return
			return productRepository.save(product);
		} else {
			throw new RuntimeException("Product not found with id " + productId);
		}
	}

//	public Product updateProduct(Long productId, String productName, String productDescription, Double price,
//			Double discountPrice, Long rating, byte[] image) {
//		Optional<Product> optionalProduct = productRepository.findById(productId);
//		if (optionalProduct.isPresent()) {
//			Product product = optionalProduct.get();
//			if (productName != null)
//				product.setProductName(productName);
//			if (productDescription != null)
//				product.setProductDescription(productDescription);
//			if (price != null)
//				product.setPrice(price);
//			if (discountPrice != null)
//				product.setDiscountPrice(discountPrice);
//			if (rating != null)
//				product.setRating(rating);
//			if (image != null)
//				product.setImage(image);
//			return productRepository.save(product);
//		} else {
//			throw new RuntimeException("Product not found with id " + productId);
//		}
//	}

//	public Optional<Product> getProductById(Long id) {
//		return productRepository.findById(id).map(this::convertToProductDto);
//	}
	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	private ProductDto convertToProductDto(Product product) {
		ProductDto dto = new ProductDto();
		dto.setProductId(product.getProductId());
		dto.setProductName(product.getProductName());
		dto.setProductDescription(product.getProductDescription());
		dto.setPrice(product.getPrice());
		dto.setDiscountPrice(product.getDiscountPrice());
		dto.setRating(product.getRating());
		dto.setCreatedAt(product.getCreatedAt());

		if (product.getImage() != null) {
			String base64Image = Base64.getEncoder().encodeToString(product.getImage());
			dto.setBase64Image("data:image/jpeg;base64," + base64Image); // Adjust MIME type as necessary
		}

		return dto;
	}

	public List<Product> getProductsByCategoryId(Long categoryId) {
		return productRepository.findByCategoryId(categoryId);
	}

}
