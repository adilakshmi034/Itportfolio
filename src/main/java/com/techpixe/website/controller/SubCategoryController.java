package com.techpixe.website.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.techpixe.website.dto.SubCategoryDto;
import com.techpixe.website.entity.SubCategory;
import com.techpixe.website.service.SubCategoryService;

@RestController
@RequestMapping("/api/subcategories")
public class SubCategoryController {
	
	   @Autowired
	    private SubCategoryService subCategoryService;

	    @PostMapping("/create/{categoryId}")
	    public ResponseEntity<SubCategory> createSubCategory(@PathVariable Long categoryId, @RequestParam String name) {
	        SubCategory subCategory = subCategoryService.createSubCategory(categoryId, name);
	        return ResponseEntity.ok(subCategory);
	    }
	    
	    @GetMapping("/all")
	    public List<SubCategory> getAllSubCategories() {
	        return subCategoryService.getAllSubCategories();
	    }
	    
	    @GetMapping("/{categoryId}")
	    public List<SubCategory> getSubCategoriesByCategoryId(@PathVariable Long categoryId) {
	        return subCategoryService.getSubCategoriesByCategoryId(categoryId);
	    }
	    
	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> deleteSubCategory(@PathVariable Long id) {
	        subCategoryService.deleteSubCategoryById(id);
	        return ResponseEntity.ok("SubCategory deleted successfully");
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<SubCategory> updateSubCategory(@PathVariable Long id, @RequestBody SubCategoryDto subCategoryDto) {
	        SubCategory updatedSubCategory = subCategoryService.updateSubCategoryById(id, subCategoryDto);
	        return ResponseEntity.ok(updatedSubCategory);
	    }
	    
	    @GetMapping("/sub/{id}")
	    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable Long id) {
	        Optional<SubCategory> subCategory = subCategoryService.getSubCategoryById(id);
	        return subCategory.map(ResponseEntity::ok)
	                          .orElseGet(() -> ResponseEntity.notFound().build());
	    }
}
