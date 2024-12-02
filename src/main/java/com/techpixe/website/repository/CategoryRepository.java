package com.techpixe.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techpixe.website.entity.Category;

public interface CategoryRepository  extends JpaRepository<Category, Long>{

	@Query("SELECT c FROM Category c WHERE c.superAdmin.id = :superAdminId")
	List<Category> findBySuperAdminId(@Param("superAdminId") Long superAdminId);


}
