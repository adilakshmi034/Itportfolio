package com.techpixe.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techpixe.website.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	 @Query("SELECT p FROM Product p WHERE p.subcategory.subCategoryId = :subCategoryId")
	    List<Product> findBySubCategoryId(@Param("subCategoryId") Long subCategoryId);

	  @Query("SELECT p FROM Product p WHERE p.subcategory.category.categoryId = :categoryId")
	    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);




}
