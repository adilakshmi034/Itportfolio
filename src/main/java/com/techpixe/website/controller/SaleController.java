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

import com.techpixe.website.dto.ErrorResponseDto;
import com.techpixe.website.dto.SalesDto;
import com.techpixe.website.entity.Sales;
import com.techpixe.website.service.SaleService;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

	@Autowired
	private SaleService saleService;
	
	

	@PostMapping("/{adminId}")
	public SalesDto createSale(@PathVariable Long adminId, @RequestBody SalesDto salesDto) {

		return saleService.createSale(adminId, salesDto);
	}

	@PostMapping("/addby/{superAdminId}")
	public SalesDto createSaleBySuperAdmin(@PathVariable Long superAdminId, @RequestBody SalesDto salesDto) {
		return saleService.createSales(superAdminId, salesDto);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String emailOrMobileNumber, @RequestParam String password) {
		if (emailOrMobileNumber != null) {
			if (isEmail(emailOrMobileNumber)) {
				return saleService.loginByEmail(emailOrMobileNumber, password);
			} else if (isMobileNumber(emailOrMobileNumber)) {
				return saleService.loginByMobileNumber(Long.parseLong(emailOrMobileNumber), password);
			} else {
				ErrorResponseDto errorResponse = new ErrorResponseDto();
				errorResponse
						.setError("Invalid emailOrMobileNumber format. Please provide a valid email or mobile number.");
				return ResponseEntity.internalServerError().body(errorResponse);
			}
		} else {
			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse
			.setError("Invalid input. Email or mobile number must be provided.");
			return ResponseEntity.internalServerError().body(errorResponse);
		}

	}

	private boolean isEmail(String emailOrMobileNumber) {
		return emailOrMobileNumber.contains("@");
	}

	private boolean isMobileNumber(String emailOrMobileNumber) {
		return emailOrMobileNumber.matches("\\d+");
	}

	@PostMapping("/changepassword/{sales_Id}")
	public ResponseEntity<?> changePassword(@PathVariable Long sales_Id, @RequestParam String password,
			@RequestParam String confirmPassword) {
		if (password != null && confirmPassword != null) {
			return saleService.changePassword(sales_Id, password, confirmPassword);
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
				return saleService.forgotPassword(email);
			} else {
				ErrorResponseDto error = new ErrorResponseDto();
				error.setError("**** *****Invalid Email Pattern *************");
				return ResponseEntity.internalServerError().body(error);
			}
		} else {
			ErrorResponseDto error = new ErrorResponseDto();
			error.setError("*********Email is not present*************");
			return ResponseEntity.internalServerError().body(error);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<Sales>> getAllSales() {
		List<Sales> sales = saleService.getAllSales();
		return new ResponseEntity<>(sales, HttpStatus.OK);
	}

	@GetMapping("/superAdmin/{superAdminId}")
	public ResponseEntity<List<SalesDto>> getSalesBySuperAdminId(@PathVariable Long superAdminId) {
		List<SalesDto> salesList = saleService.getSalesBySuperAdminId(superAdminId);
		return ResponseEntity.ok(salesList);
	}

	// API to get sales by adminId
	@GetMapping("/admin/{adminId}")
	public ResponseEntity<List<Sales>> getSalesByAdminId(@PathVariable Long adminId) {
		List<Sales> salesList = saleService.getSalesByAdminId(adminId);
		return ResponseEntity.ok(salesList);
	}

	@DeleteMapping("/{salesId}")
	public ResponseEntity<String> deleteSalesById(@PathVariable Long salesId) {
		try {
			saleService.deleteSalesById(salesId);
			return ResponseEntity.ok("Sales entity deleted successfully");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Sales> updateSales(@PathVariable("id") Long id, @RequestBody SalesDto salesDto) {
		System.err.println(salesDto);
		Sales updatedSales = saleService.updateSales(id, salesDto);
		return ResponseEntity.ok(updatedSales);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SalesDto> getSalesById(@PathVariable Long id) {
		Optional<SalesDto> sales = saleService.getSalesById(id);
		if (sales.isPresent()) {
			return new ResponseEntity<>(sales.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
