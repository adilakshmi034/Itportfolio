package com.techpixe.website.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techpixe.website.dto.SubCategoryDto;
import com.techpixe.website.entity.Category;
import com.techpixe.website.entity.SubCategory;
import com.techpixe.website.repository.CategoryRepository;
import com.techpixe.website.repository.SubCategoryRepository;
import com.techpixe.website.service.SubCategoryService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{

	@Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public SubCategory createSubCategory(Long categoryId, String subCategoryName) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        SubCategory subCategory = new SubCategory();
        subCategory.setName(subCategoryName);
        subCategory.setCategory(category);
 
        return subCategoryRepository.save(subCategory);
    }
    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }
    
    public List<SubCategory> getSubCategoriesByCategoryId(Long categoryId) {
        return subCategoryRepository.findByCategoryCategoryId(categoryId);
    }
    
    public void deleteSubCategoryById(Long id) {
        subCategoryRepository.deleteById(id);
    }
    // Update SubCategory with Request Params
    public SubCategory updateSubCategoryById(Long id, SubCategoryDto subCategoryDto) {
        // Find the existing subcategory by id
        SubCategory existingSubCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found with id: " + id));
        
        // Update the subcategory fields
        existingSubCategory.setName(subCategoryDto.getName());

        // Save the updated subcategory back to the database
        return subCategoryRepository.save(existingSubCategory);
    }
    
    public Optional<SubCategory> getSubCategoryById(Long id) {
        return subCategoryRepository.findById(id);
    }
	
}
