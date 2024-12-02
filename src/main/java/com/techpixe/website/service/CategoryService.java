package com.techpixe.website.service;

import java.util.List;

import com.techpixe.website.dto.CategoryDto;
import com.techpixe.website.entity.Category;

public interface CategoryService {

	Category createCategory(String name, Long superAdminId);

	//List<Category> getAllCategories();

	void deleteCategory(Long id);

	List<Category> getCategoriesBySuperAdminId(Long superAdminId);

	//Optional<Category> getCategoryById(Long id);


	Category updateCategory(Long id, CategoryDto category);

	List<Category> getAllCategories();

	Category getCategoryById(Long categoryId);

}
