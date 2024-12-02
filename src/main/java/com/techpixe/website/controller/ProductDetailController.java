package com.techpixe.website.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techpixe.website.entity.ProductDetails;
import com.techpixe.website.entity.ProductFeature;
import com.techpixe.website.entity.ProductImage;
import com.techpixe.website.service.productDetailsService;
import com.fasterxml.jackson.core.type.TypeReference;

@RestController
@RequestMapping("/api/productdetails")
public class ProductDetailController {

	@Autowired
	productDetailsService ProductDetailService;

	@PostMapping("/{productId}")
	public ResponseEntity<ProductDetails> createProductDetails(@PathVariable Long productId,
			@RequestParam("name") String name, @RequestParam("description") String description,
			@RequestParam("features") List<String> features,
			@RequestParam(value = "images", required = false) List<MultipartFile> images,
			@RequestParam(value = "imageDescriptions", required = false) List<String> imageDescriptions,
			@RequestParam(value = "videos", required = false) List<MultipartFile> videos,
			@RequestParam(value = "videoDescriptions", required = false) List<String> videoDescriptions)
			throws IOException {

		// Call the service to create ProductDetails
		ProductDetails savedProductDetails = ProductDetailService.createProductDetails(productId, name, description,
				features, images, imageDescriptions, videos, videoDescriptions);

		return ResponseEntity.ok(savedProductDetails);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDetails> updateProductDetails(@PathVariable Long id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "features", required = false) String featuresJson,
			@RequestParam(value = "images", required = false) String imagesJson) throws IOException {

		try {

			List<Map<String, Object>> features = new ObjectMapper().readValue(featuresJson, new TypeReference<>() {
			});
			List<Map<String, Object>> mediaList = new ObjectMapper().readValue(imagesJson,
					new TypeReference<List<Map<String, Object>>>() {
					});

			// Fetch the existing ProductDetails by ID
			ProductDetails existingProductDetails = ProductDetailService.getProductDetailsById(id);

			// Call the service to update ProductDetails
			ProductDetails updatedProductDetails = ProductDetailService.updateProductDetails(existingProductDetails,
					name, description, features, mediaList);
			return ResponseEntity.ok(updatedProductDetails);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProductDetails(@PathVariable Long id) {
		try {
			ProductDetailService.deleteProductDetails(id);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Product details and associated records deleted successfully");
		} catch (RuntimeException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ProductDetails> getProductDetails(@PathVariable Long productId) {
		ProductDetails productDetails = ProductDetailService.getProductDetailsByProductId(productId);

		if (productDetails != null) {
			return new ResponseEntity<>(productDetails, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/details/{id}")
	public ResponseEntity<ProductDetails> getProductDetailsById(@PathVariable("id") Long id) {
		ProductDetails productDetails = ProductDetailService.getProductDetailsById(id);

		if (productDetails != null) {
			return new ResponseEntity<>(productDetails, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
