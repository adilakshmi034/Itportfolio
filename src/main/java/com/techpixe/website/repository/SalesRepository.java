package com.techpixe.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techpixe.website.entity.Sales;

public interface SalesRepository extends JpaRepository<Sales, Long> {

	Sales findByMobileNumber(Long mobileNumber);

	Sales findByEmail(String email);

	@Query("SELECT s FROM Sales s WHERE s.superAdmin.id = :id")
	List<Sales> findSalesBySuperAdminId(@Param("id") Long superAdminId);

	// Custom query to get sales by adminId
	@Query("SELECT s FROM Sales s WHERE s.admin.admin_Id = :admin_Id")
	List<Sales> findSalesByAdminId(@Param("admin_Id") Long adminId);

	boolean existsByEmail(String email);

	boolean existsByMobileNumber(Long mobileNumber);
}
