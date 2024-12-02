package com.techpixe.website.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.website.dto.AdminDto;
import com.techpixe.website.dto.ErrorResponseDto;
import com.techpixe.website.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

	@Autowired
	private AdminService adminService;


	@PostMapping("/{superAdminId}")
	public AdminDto createAdmin(@PathVariable Long superAdminId,   @RequestBody AdminDto adminDto) {

		return adminService.createAdmin(superAdminId, adminDto);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String emailOrMobileNumber, @RequestParam String password) {
		if (emailOrMobileNumber != null) {
			if (isEmail(emailOrMobileNumber)) {
				return adminService.loginByEmail(emailOrMobileNumber, password);
			} else if (isMobileNumber(emailOrMobileNumber)) {
				return adminService.loginByMobileNumber(Long.parseLong(emailOrMobileNumber), password);
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

	@PostMapping("/changepassword/{admin_Id}")
	public ResponseEntity<?> changePassword(@PathVariable Long admin_Id, @RequestParam String password,
			@RequestParam String confirmPassword) {
		if (password != null && confirmPassword != null) {
			return adminService.changePassword(admin_Id, password, confirmPassword);
		} else {
			ErrorResponseDto error = new ErrorResponseDto();
			error.setError("*********Password is not present*************");
			return ResponseEntity.internalServerError().body(error);
		}
	}


	// *************FORGOT PASSWORD****************
	@PostMapping("/forgotpassword")
	public ResponseEntity<?> forgotPassword(@RequestParam String email) {
	    if (email != null) {
	        if (isEmail(email)) {
	            return adminService.processForgotPassword(email); // Updated to a unique name for clarity
	        } else {
	            ErrorResponseDto error = new ErrorResponseDto();
	            error.setError("Invalid Email Pattern");
	            return ResponseEntity.badRequest().body(error);
	        }
	    } else {
	        ErrorResponseDto error = new ErrorResponseDto();
	        error.setError("Email is not present");
	        return ResponseEntity.badRequest().body(error);
	    }
	}


	@GetMapping("/all")
	public ResponseEntity<List<AdminDto>> getAllAdmins() {
		List<AdminDto> admins = adminService.getAllAdmins();
		return new ResponseEntity<>(admins, HttpStatus.OK);
	}

	@GetMapping("/{superAdminId}/admins")
	public ResponseEntity<List<AdminDto>> getAdminsBySuperAdminId(@PathVariable Long superAdminId) {
		List<AdminDto> admins = adminService.getAdminsBySuperAdminId(superAdminId);
		return ResponseEntity.ok(admins);
	}

	@PutMapping("/{id}")
	public AdminDto updateAdmin(@PathVariable Long id, @RequestBody AdminDto adminDetails) {
	    return adminService.updateAdmin(id, adminDetails);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AdminDto> getAdminById(@PathVariable("id") Long adminId) {
		Optional<AdminDto> admin = adminService.getAdminById(adminId);
		if (admin.isPresent()) {
			return ResponseEntity.ok(admin.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAdmin(@PathVariable("id") Long id) {
		try {
			adminService.deleteAdminById(id);
			return ResponseEntity.ok("Admin deleted successfully");
		} catch (RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
}
