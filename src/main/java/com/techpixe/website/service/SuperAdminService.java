package com.techpixe.website.service;

import org.springframework.http.ResponseEntity;

import com.techpixe.website.entity.SuperAdmin;

public interface SuperAdminService {
	SuperAdmin registerAdmin(String fullName, String email, Long mobileNumber, String password);

	ResponseEntity<?> loginByMobileNumber(Long mobileNumber, String password);

	ResponseEntity<?> loginByEmail(String email, String password);

	ResponseEntity<?> changePassword(Long id, String password, String confirmPassword);

	ResponseEntity<?> forgotPassword(String email);

}
