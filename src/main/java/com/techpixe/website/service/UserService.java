package com.techpixe.website.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.techpixe.website.entity.User;

public interface UserService {

	User createUser(Long superAdminId, String fullName, String email, Long mobileNumber);

	ResponseEntity<?> loginByEmail(String emailOrMobileNumber, String password);

	ResponseEntity<?> changePassword(Long userId, String password, String confirmPassword);

	ResponseEntity<?> forgotPassword(String email);

	ResponseEntity<?> loginByMobileNumber(Long mobileNumber, String password);

	List<User> getAllAdmins();

//	List<User> getUsersBySuperAdminId(Long superAdminId);

}