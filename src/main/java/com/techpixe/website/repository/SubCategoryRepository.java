package com.techpixe.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.website.entity.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>{

	List<SubCategory> findByCategoryCategoryId(Long categoryId);

}
