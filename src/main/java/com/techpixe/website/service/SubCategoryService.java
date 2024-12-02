package com.techpixe.website.service;

import java.util.List;
import java.util.Optional;

import com.techpixe.website.dto.SubCategoryDto;
import com.techpixe.website.entity.SubCategory;

public interface SubCategoryService {

	SubCategory createSubCategory(Long categoryId, String name);

	List<SubCategory> getAllSubCategories();

	List<SubCategory> getSubCategoriesByCategoryId(Long categoryId);

	void deleteSubCategoryById(Long id);


	SubCategory updateSubCategoryById(Long id, SubCategoryDto subCategoryDto);

	Optional<SubCategory> getSubCategoryById(Long id);

}
