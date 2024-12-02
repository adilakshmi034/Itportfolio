package com.techpixe.website.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.website.dto.CategoryDto;
import com.techpixe.website.entity.Category;
import com.techpixe.website.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@PostMapping("/create/{superAdminId}")
	public ResponseEntity<Category> createCategory(@PathVariable Long superAdminId, @RequestParam String name) {

		Category savedCategory = categoryService.createCategory(name, superAdminId);
		return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Category>> getAllCategories() {
	    try {
	        List<Category> categories = categoryService.getAllCategories();
	        return ResponseEntity.ok(categories);
	    } catch (Exception e) {
	      //  logger.error("Error retrieving categories: {}", e.getMessage(), e);
	        System.out.println("Error retrieving categories: {}");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

	
    @GetMapping("/{superAdminId}")
    public ResponseEntity<List<Category>> getCategoriesBySuperAdminId(@PathVariable Long superAdminId) {
        List<Category> categories = categoryService.getCategoriesBySuperAdminId(superAdminId);
        return ResponseEntity.ok(categories);
    }


	@DeleteMapping("/{id}")
	public void deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
	}
//	@GetMapping("/categories/{id}")
//	public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
//	    Optional<Category> category = categoryService.getCategoryById(id);
//	    
//	    if (category.isPresent()) {
//	        Category foundCategory = category.get();
//	        // Check if the products list is not empty
//	        if (!foundCategory.getProducts().isEmpty()) {
//	            System.out.println(foundCategory.getProducts().get(0));
//	        } else {
//	            System.out.println("No products found for this category.");
//	        }
//	        return ResponseEntity.ok(foundCategory);
//	    } else {
//	        return ResponseEntity.notFound().build();
//	    }
//	}

	 
	 @PutMapping("/{id}")
	    public ResponseEntity<Category> updateCategoryById(
	            @PathVariable("id") Long id, 
	            @RequestBody CategoryDto category) {

	        Category updatedCategory = categoryService.updateCategory(id, category);
	        if (updatedCategory == null) {
	            return ResponseEntity.notFound().build();
	        }

	        return ResponseEntity.ok(updatedCategory);
	    }
	 
	 @GetMapping("/category/{id}")
	    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long categoryId) {
	        Category category = categoryService.getCategoryById(categoryId);
	        return ResponseEntity.ok(category);
	    }

}
