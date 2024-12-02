package com.techpixe.website.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techpixe.website.dto.ProductDto;
import com.techpixe.website.entity.Product;
import com.techpixe.website.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	 @PostMapping("/create/{subCategoryId}")
	    public ResponseEntity<Product> createProduct(@RequestParam("image") MultipartFile imageFile,
	                                                 @RequestParam("productName") String productName,
	                                                 @RequestParam("productDescription") String productDescription,
	                                                 @RequestParam("price") double price,
	                                                 @RequestParam("discountPrice") double discountPrice,
	                                                 @RequestParam("rating") Long rating,
	                                                 @PathVariable Long subCategoryId) {
	        // Convert MultipartFile to byte[]
	        byte[] imageBytes;
	        try {
	            imageBytes = imageFile.getBytes();
	        } catch (IOException e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle image upload error
	        }

	        // Create ProductDto with the received parameters
	        ProductDto productDto = new ProductDto();
	        productDto.setProductName(productName);
	        productDto.setProductDescription(productDescription);
	        productDto.setPrice(price);
	        productDto.setDiscountPrice(discountPrice);
	        productDto.setRating(rating);
	        productDto.setImage(imageBytes);

	        // Call service to create the product with subCategoryId
	        try {
	            Product createdProduct = productService.createProduct(productDto, subCategoryId);
	            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Handle creation error
	        }
	    }
	
	@GetMapping("/subcategory/{subCategoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long subCategoryId) {
        return productService.getProductsByCategory(subCategoryId);
    }
	
	 @DeleteMapping("/{id}")
	    public void deleteProduct(@PathVariable Long id) {
		 productService.deleteProduct(id);
	    }


	@GetMapping("/all")
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		return ResponseEntity.ok(products);
	}

	@PutMapping("update/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String productDescription,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double discountPrice,
            @RequestParam(required = false) Long rating,
            @RequestParam(required = false) MultipartFile image) {

        byte[] imageBytes = null;
        if (image != null && !image.isEmpty()) {
            try {
                imageBytes = image.getBytes();
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        Product updatedProduct = productService.updateProduct(productId, productName, productDescription, price, discountPrice, rating, imageBytes);
        return ResponseEntity.ok(updatedProduct);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
	 @GetMapping("/category/{categoryId}")
	    public List<Product> getProductsByCategoryId(@PathVariable Long categoryId) {
	        return productService.getProductsByCategoryId(categoryId);
	    }
	


}
