package com.techpixe.website.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.website.dto.ErrorResponseDto;
import com.techpixe.website.entity.User;
import com.techpixe.website.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/{superAdminId}")
	public User createAdmin(@PathVariable Long superAdminId, @RequestParam String fullName, @RequestParam String email,
			@RequestParam Long mobileNumber) {

		return userService.createUser(superAdminId, fullName, email, mobileNumber);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String emailOrMobileNumber, @RequestParam String password) {
		if (emailOrMobileNumber != null) {
			if (isEmail(emailOrMobileNumber)) {
				return userService.loginByEmail(emailOrMobileNumber, password);
			} else if (isMobileNumber(emailOrMobileNumber)) {
				return userService.loginByMobileNumber(Long.parseLong(emailOrMobileNumber), password);
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

	@PostMapping("/changepassword/{userId}")
	public ResponseEntity<?> changePassword(@PathVariable Long userId, @RequestParam String password,
			@RequestParam String confirmPassword) {
		if (password != null && confirmPassword != null) {
			return userService.changePassword(userId, password, confirmPassword);
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
				return userService.forgotPassword(email);
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

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllAdmins() {
		List<User> users = userService.getAllAdmins();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
//	 @GetMapping("/superadmin/{superAdminId}")
//	    public ResponseEntity<List<User>> getUsersBySuperAdminId(@PathVariable Long superAdminId) {
//	        List<User> users = userService.getUsersBySuperAdminId(superAdminId);
//	        if (users.isEmpty()) {
//	            return ResponseEntity.noContent().build();
//	        }
//	        return ResponseEntity.ok(users);
//	    } 
	

}
