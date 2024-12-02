package com.techpixe.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpixe.website.entity.SuperAdmin;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Long> {

	SuperAdmin findByMobileNumber(Long mobileNumber);

	SuperAdmin findByEmail(String email);

}
