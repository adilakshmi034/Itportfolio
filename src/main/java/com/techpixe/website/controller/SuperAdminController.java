package com.techpixe.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.website.dto.ErrorResponseDto;
import com.techpixe.website.entity.SuperAdmin;
import com.techpixe.website.service.SuperAdminService;

@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {

	@Autowired
	private SuperAdminService superAdminService;

	@PostMapping("/registration")
	public ResponseEntity<SuperAdmin> registerAdmin(@RequestParam String fullName, @RequestParam String email,
			@RequestParam Long mobileNumber, @RequestParam String password) {

		SuperAdmin registeredAdmin = superAdminService.registerAdmin(fullName, email, mobileNumber, password);
		return new ResponseEntity<SuperAdmin>(registeredAdmin, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String emailOrMobileNumber, @RequestParam String password) {
		if (emailOrMobileNumber != null) {
			if (isEmail(emailOrMobileNumber)) {
				return superAdminService.loginByEmail(emailOrMobileNumber, password);
			} else if (isMobileNumber(emailOrMobileNumber)) {
				return superAdminService.loginByMobileNumber(Long.parseLong(emailOrMobileNumber), password);
			} else {
				ErrorResponseDto errorResponse = new ErrorResponseDto();
				errorResponse
						.setError("Invalid emailOrMobileNumber format. Please provide a valid email or mobile number.");
				return ResponseEntity.internalServerError().body(errorResponse);
			}
			
		} else {
			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setError("Invalid input. Email or mobile number must be provided.");
			return ResponseEntity.internalServerError().body(errorResponse);
		}

	}

	private boolean isEmail(String emailOrMobileNumber) {
		return emailOrMobileNumber.contains("@");
	}

	private boolean isMobileNumber(String emailOrMobileNumber) {
		return emailOrMobileNumber.matches("\\d+");
	}
	
	@PostMapping("/changepassword/{id}")
	public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestParam String password,
			@RequestParam String confirmPassword) {
		if (password != null && confirmPassword != null) {
			return superAdminService.changePassword(id, password, confirmPassword);
		} else {
			ErrorResponseDto error = new ErrorResponseDto();
			error.setError("*********Password is not present*************");
			return ResponseEntity.internalServerError().body(error);
		}
	}

	// *************FORGOT PASSWORD****************
	@PostMapping("/forgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestParam String email) {
		if (email != null) {
			if (isEmail(email)) {
				return superAdminService.forgotPassword(email);
			} else {
				ErrorResponseDto error = new ErrorResponseDto();
				error.setError("*********Invalid Email Pattern *************");
				return ResponseEntity.internalServerError().body(error);
			}
		} else { 
			ErrorResponseDto error = new ErrorResponseDto();
			error.setError("*********Email is not present*************");
			return ResponseEntity.internalServerError().body(error);
		}
	}

}
