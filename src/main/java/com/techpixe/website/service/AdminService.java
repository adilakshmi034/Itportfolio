package com.techpixe.website.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.techpixe.website.dto.AdminDto;

public interface AdminService {

	ResponseEntity<?> loginByEmail(String emailOrMobileNumber, String password);

	//ResponseEntity<?> forgotPassword(String email);

	ResponseEntity<?> loginByMobileNumber(Long mobileNumber, String password);

	ResponseEntity<?> changePassword(Long admin_Id, String password, String confirmPassword);

	AdminDto createAdmin(Long superAdminId, AdminDto adminDto);

	List<AdminDto> getAllAdmins();

	// Optional<Admin> getAdminsById(Long superAdminId);

	List<AdminDto> getAdminsBySuperAdminId(Long superAdminId);

	AdminDto updateAdmin(Long id, AdminDto adminDetails);

	Optional<AdminDto> getAdminById(Long adminId);

	void deleteAdminById(Long id);

	ResponseEntity<?> processForgotPassword(String email);

}
