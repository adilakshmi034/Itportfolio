package com.techpixe.website.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techpixe.website.dto.CategoryDto;
import com.techpixe.website.entity.Category;
import com.techpixe.website.entity.SuperAdmin;
import com.techpixe.website.repository.CategoryRepository;
import com.techpixe.website.repository.SuperAdminRepository;
import com.techpixe.website.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SuperAdminRepository superAdminRepository;

	public Category createCategory(String name, Long superAdminId) {
		SuperAdmin superAdmin = superAdminRepository.findById(superAdminId)
				.orElseThrow(() -> new RuntimeException("SuperAdmin not found"));

		Category category = new Category();
		category.setName(name);
		// category.setLink(link);
		category.setSuperAdmin(superAdmin);

		return categoryRepository.save(category);
	}

//	    public List<Category> getAllCategories() {
//	        return categoryRepository.findAll();
//	    }

	public void deleteCategory(Long id) {
		categoryRepository.deleteById(id);
	}

	public List<Category> getCategoriesBySuperAdminId(Long superAdminId) {
		List<Category> list = categoryRepository.findBySuperAdminId(superAdminId);

		return list;
	}

//	    public Optional<Category> getCategoryById(Long id) {
//	        return categoryRepository.findById(id); 
//	    }

	@Override
	public Category updateCategory(Long id, CategoryDto category) {
		Optional<Category> existingCategoryOpt = categoryRepository.findById(id);

		if (existingCategoryOpt.isPresent()) {
			Category existingCategory = existingCategoryOpt.get();

			// Update only the name field from the DTO
			existingCategory.setName(category.getName());

			// Save the updated category entity
			return categoryRepository.save(existingCategory);
		}

		return null; // Return null if category is not found

	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}

	public Category getCategoryById(Long categoryId) {
		Optional<Category> category = categoryRepository.findById(categoryId);
		return category.orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
	}

}
