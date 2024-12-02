package com.techpixe.website.serviceimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techpixe.website.config.DuplicateSalesException;
import com.techpixe.website.config.InvalidMobileNumberException;
import com.techpixe.website.constants.PasswordGenerator;
import com.techpixe.website.dto.ErrorResponseDto;
import com.techpixe.website.dto.SalesDto;
import com.techpixe.website.entity.Admin;
import com.techpixe.website.entity.Sales;
import com.techpixe.website.entity.SuperAdmin;
import com.techpixe.website.repository.AdminRepository;
import com.techpixe.website.repository.SalesRepository;
import com.techpixe.website.repository.SuperAdminRepository;
import com.techpixe.website.service.SaleService;

@Service
public class SalesServiceImpl implements SaleService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private SalesRepository salesRepository;

	@Autowired
	private SuperAdminRepository superAdminRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("$(spring.mail.username)")
	private String fromMail;

	private String appBaseUrl = "http://localhost:3000"; // Replace with your actual application base URL
	private String changePasswordUrl = appBaseUrl + "/changepassword";

	@Override
	public SalesDto createSale(Long adminId, SalesDto salesDto) {
		Admin admin = adminRepository.findById(adminId)
				.orElseThrow(() -> new RuntimeException("Admin not found with id " + adminId));

		// Check if email already exists
		if (salesRepository.existsByEmail(salesDto.getEmail())) {
			throw new DuplicateSalesException("Salesperson already exists with the provided email.");
		}

		// Check if mobile number already exists
		if (salesRepository.existsByMobileNumber(salesDto.getMobileNumber())) {
			throw new DuplicateSalesException("Salesperson already exists with the provided mobile number.");
		}

		// Validate mobile number format
		String mobileNumber = String.valueOf(salesDto.getMobileNumber());
		if (mobileNumber.length() != 10 || !mobileNumber.matches("\\d{10}")) {
			throw new InvalidMobileNumberException("Mobile number must be exactly 10 digits.");
		}

		String generatedPassword = PasswordGenerator.generatePassword(12); // Adjust the length as needed

		Sales sales = new Sales();
		sales.setFullName(salesDto.getFullName());
		sales.setEmail(salesDto.getEmail());
		sales.setMobileNumber(salesDto.getMobileNumber());
		sales.setPassword(passwordEncoder.encode(generatedPassword));
		sales.setAdmin(admin);
		sales.setRole("sales");
		sales.setCreatedDate(LocalDate.now());

		Sales savedSales = salesRepository.save(sales);

		sendEmailToAdmin(sales.getEmail(), generatedPassword);

		return convertToDto(savedSales); // Convert to SalesDto before returning
	}

	// Helper method to convert Sales to SalesDto
	private SalesDto convertToDto(Sales sales) {
		SalesDto salesDto = new SalesDto();
		salesDto.setSales_Id(sales.getSales_Id());
		salesDto.setFullName(sales.getFullName());
		salesDto.setEmail(sales.getEmail());
		salesDto.setMobileNumber(sales.getMobileNumber());
		salesDto.setRole(sales.getRole());
		// Add other necessary fields here if needed
		return salesDto;
	}

	private void sendEmailToAdmin(String email, String password) {
		String appBaseUrl = "http://localhost:3000"; // Replace with your actual application base URL
		String changePasswordUrl = appBaseUrl + "/changepassword";

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Your Sale user Account Details");
		message.setText("Hello,\n\nYour admin account has been created successfully.\n\n" + "Username: " + email + "\n"
				+ "Password: " + password + "\n\n"
				+ "For security reasons, please change your password upon your first login.\n"
				+ "You can reset your password here: " + changePasswordUrl + "\n\n" + "Best Regards,\nTechpixe");

		mailSender.send(message);
	}

	@Override
	public ResponseEntity<?> loginByMobileNumber(Long mobileNumber, String password) {
		password=password.trim();
		Sales sales = salesRepository.findByMobileNumber(mobileNumber);

		if (sales != null && passwordEncoder.matches(password, sales.getPassword())) {
			SalesDto salesDto = new SalesDto();
			salesDto.setSales_Id(sales.getSales_Id());
			salesDto.setFullName(sales.getFullName());
			salesDto.setEmail(sales.getEmail());
			salesDto.setMobileNumber(sales.getMobileNumber());
			return ResponseEntity.ok(salesDto);
		} else {
			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setError("Invalid mobile number or password");
			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}

	@Override
	public ResponseEntity<?> loginByEmail(String email, String password) {
		password = password.trim();
		email = email.trim();
		Sales sales = salesRepository.findByEmail(email);
		System.out.println(email);

		if (sales != null && passwordEncoder.matches(password, sales.getPassword())) {
			SalesDto salesDto = new SalesDto();
			salesDto.setSales_Id(sales.getSales_Id());
			salesDto.setFullName(sales.getFullName());
			salesDto.setEmail(sales.getEmail());
			salesDto.setMobileNumber(sales.getMobileNumber());
			// salesDto.setPassword(sales.getPassword());
			return ResponseEntity.ok(salesDto);
		} else {
			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setError("Invalid email or password");
			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}

	// ***************CHANGE PASSWORD*************************

	@Override
	public ResponseEntity<?> changePassword(Long sales_Id, String password, String confirmPassword) {
		Sales sales = salesRepository.findById(sales_Id)
				.orElseThrow(() -> new RuntimeException("sale is not present: " + sales_Id));

		if (sales != null && passwordEncoder.matches(password, sales.getPassword())) {

			System.out.println("user Password is Successfully Changed");

			SalesDto salesDto = new SalesDto();
			salesDto.setSales_Id(sales.getSales_Id());
			salesDto.setFullName(sales.getFullName());
			salesDto.setEmail(sales.getEmail());
			salesDto.setMobileNumber(sales.getMobileNumber());
			// salesDto.setPassword(confirmPassword);

			sales.setPassword(passwordEncoder.encode(confirmPassword));
			salesRepository.save(sales);
			return ResponseEntity.ok(salesDto);

		} else {

			System.out.println("Password and Confirm Password are not Matching");

			ErrorResponseDto error = new ErrorResponseDto();
			error.setError("######################Password is not present#################");
			// return ResponseEntity.internalServerError().body(error);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
		}
	}

	@Override
	public ResponseEntity<?> forgotPassword(String email) {
		Sales sales = salesRepository.findByEmail(email);

		if (sales != null) {

			System.out.println("User : Password will be sent to E-Mail Number");

			SalesDto salesDto = new SalesDto();
			salesDto.setSales_Id(sales.getSales_Id());
			salesDto.setFullName(sales.getFullName());
			salesDto.setEmail(email);
			salesDto.setMobileNumber(sales.getMobileNumber());
			// salesDto.setPassword(sales.getPassword());

			String password = PasswordGenerator.generatePassword(12);
			// salesDto.setPassword(password);

			sales.setPassword(passwordEncoder.encode(password));
			salesRepository.save(sales);

			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(fromMail);
			simpleMailMessage.setTo(email);
			simpleMailMessage.setSubject("password changed Successfully in E_HR application\n");
			simpleMailMessage.setText("Hello,\n\nYour sales user  account has been created successfully.\n\n"
					+ "Username: " + email + "\n" + "Password: " + password + "\n\n"
					+ "For security reasons, please change your password upon your first login.\n"
					+ "You can reset your password here: " + changePasswordUrl + "\n\n" + "Best Regards,\nTechpixe");
			mailSender.send(simpleMailMessage);

			return ResponseEntity.ok(salesDto);
		} else {

			System.out.println("****Invalid Email****");

			ErrorResponseDto errorResponseDto = new ErrorResponseDto();
			errorResponseDto.setError("Email is not matching");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
		}
	}

	@Override
	public List<Sales> getAllSales() {
		return salesRepository.findAll();
	}
	@Override
	public SalesDto createSales(Long superAdminId, SalesDto salesDto) {
	    // Fetch SuperAdmin by ID
	    SuperAdmin superAdmin = superAdminRepository.findById(superAdminId)
	            .orElseThrow(() -> new RuntimeException("Admin not found with id " + superAdminId));

	    // Check if email already exists
	    if (salesRepository.existsByEmail(salesDto.getEmail())) {
	        throw new DuplicateSalesException("Salesperson already exists with the provided email.");
	    }

	    // Check if mobile number already exists
	    if (salesRepository.existsByMobileNumber(salesDto.getMobileNumber())) {
	        throw new DuplicateSalesException("Salesperson already exists with the provided mobile number.");
	    }

	    // Validate mobile number format
	    String mobileNumber = String.valueOf(salesDto.getMobileNumber());
	    if (mobileNumber.length() != 10 || !mobileNumber.matches("\\d{10}")) {
	        throw new InvalidMobileNumberException("Mobile number must be exactly 10 digits.");
	    }

	    // Generate a password
	    String generatedPassword = PasswordGenerator.generatePassword(12); // Adjust length as needed

	    // Create and populate Sales entity
	    Sales sales = new Sales();
	    sales.setFullName(salesDto.getFullName());
	    sales.setEmail(salesDto.getEmail());
	    sales.setMobileNumber(salesDto.getMobileNumber());
	    sales.setPassword(passwordEncoder.encode(generatedPassword));
	    sales.setSuperAdmin(superAdmin);
	    sales.setRole("sales");

	    // Save Sales entity
	    Sales savedSales = salesRepository.save(sales);

	    // Send email notification
	    sendEmailToAdmin(sales.getEmail(), generatedPassword);

	    // Convert the saved Sales entity to SalesDto
	    return convertToDto(savedSales);
	}


	public List<SalesDto> getSalesBySuperAdminId(Long superAdminId) {
		return salesRepository.findSalesBySuperAdminId(superAdminId).stream().map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public List<Sales> getSalesByAdminId(Long adminId) {
		return salesRepository.findSalesByAdminId(adminId);
	}

	private SalesDto mapToDto(Sales sale) {
		SalesDto salesDto = new SalesDto();
		salesDto.setSales_Id(sale.getSales_Id()); // Assuming sale.getId() returns sales_Id
		salesDto.setFullName(sale.getFullName());
		salesDto.setEmail(sale.getEmail());
		salesDto.setMobileNumber(sale.getMobileNumber());
		salesDto.setRole(sale.getRole());
		return salesDto;
	}

	public void deleteSalesById(Long salesId) {
		if (salesRepository.existsById(salesId)) {
			salesRepository.deleteById(salesId);
		} else {
			throw new IllegalArgumentException("Sales entity with ID " + salesId + " does not exist.");
		}
	}

	@Override
	public Sales updateSales(Long id, SalesDto salesDto) {
		Optional<Sales> existingSales = salesRepository.findById(id);
		if (existingSales.isPresent()) {
			Sales sales = existingSales.get();
			sales.setFullName(salesDto.getFullName());
			sales.setEmail(salesDto.getEmail());
			sales.setMobileNumber(salesDto.getMobileNumber());
			sales.setRole(salesDto.getRole());
			return salesRepository.save(sales);
		} else {
			throw new RuntimeException("Sales not found with id " + id);
		}
	}

	public Optional<SalesDto> getSalesById(Long id) {
		return salesRepository.findById(id).map(this::mapToDto); // Convert Sales to SalesDto if present
	}

}
