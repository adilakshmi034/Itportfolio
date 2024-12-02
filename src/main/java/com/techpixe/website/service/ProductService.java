package com.techpixe.website.service;

import java.util.List;
import java.util.Optional;

import com.techpixe.website.dto.ProductDto;
import com.techpixe.website.entity.Product;

public interface ProductService {

	List<Product> getAllProducts();

	void deleteProduct(Long id);

	Product updateProduct(Long productId, String productName, String productDescription, Double price,
			Double discountPrice, Long rating, byte[] image);

	Optional<Product> getProductById(Long id);

	Product createProduct(ProductDto productDto, Long subCategoryId);

	List<Product> getProductsByCategory(Long subCategoryId);

	List<Product> getProductsByCategoryId(Long categoryId);

}
