package com.techpixe.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.website.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	Admin findByMobileNumber(Long mobileNumber);

	Admin findByEmail(String email);

	List<Admin> findBySuperAdminId(Long superAdminId);

	boolean existsByEmail(String email); // Define method to check email existence

    boolean existsByMobileNumber(Long mobileNumber); 
}
