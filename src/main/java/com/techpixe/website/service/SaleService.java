package com.techpixe.website.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.techpixe.website.dto.SalesDto;
import com.techpixe.website.entity.Sales;

public interface SaleService {

//	Sales createSale(Long adminId, String fullName, String email, Long mobileNumber);

	ResponseEntity<?> loginByEmail(String emailOrMobileNumber, String password);

	ResponseEntity<?> forgotPassword(String email);

	ResponseEntity<?> changePassword(Long sales_Id, String password, String confirmPassword);

	ResponseEntity<?> loginByMobileNumber(Long mobileNumber, String password);

	List<Sales> getAllSales();

	// Sales createSales(Long superAdminId, String fullName, String email, Long
	// mobileNumber);

	SalesDto createSales(Long superAdminId, SalesDto salesDto);

	List<SalesDto> getSalesBySuperAdminId(Long superAdminId);

	List<Sales> getSalesByAdminId(Long adminId);

	SalesDto createSale(Long adminId, SalesDto salesDto);

	void deleteSalesById(Long salesId);

	Sales updateSales(Long id, SalesDto salesDto);

	Optional<SalesDto> getSalesById(Long id);

}
