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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techpixe.website.config.DuplicateSalesException;
import com.techpixe.website.config.InvalidMobileNumberException;
import com.techpixe.website.constants.PasswordGenerator;
import com.techpixe.website.dto.AdminDto;
import com.techpixe.website.dto.ErrorResponseDto;
import com.techpixe.website.entity.Admin;
import com.techpixe.website.entity.SuperAdmin;
import com.techpixe.website.repository.AdminRepository;
import com.techpixe.website.repository.SuperAdminRepository;
import com.techpixe.website.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private SuperAdminRepository superAdminRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private JavaMailSender mailSender;

	private String appBaseUrl = "http://localhost:3000"; // Replace with your actual application base URL
	private String changePasswordUrl = appBaseUrl + "/changepassword";

	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@Value("$(spring.mail.username)")
	private String fromMail;

	public AdminDto createAdmin(Long superAdminId, AdminDto adminDto) {
	    SuperAdmin superAdmin = superAdminRepository.findById(superAdminId)
	            .orElseThrow(() -> new RuntimeException("SuperAdmin not found with id " + superAdminId));

	 
	    if (adminRepository.existsByEmail(adminDto.getEmail())) {
	        throw new DuplicateSalesException("Admin already exist with the provided email.");
	    }
	    

	  
	    if (adminRepository.existsByMobileNumber(adminDto.getMobileNumber())) {
	        throw new DuplicateSalesException("Admin already exist with the provided mobile number.");
	    }
  


	  
	    String mobileNumber = String.valueOf(adminDto.getMobileNumber());
	    if (mobileNumber.length() != 10) {
	    	System.err.println(mobileNumber.length());
	        throw new InvalidMobileNumberException("Mobile number must be exactly 10 digits.");
	    }

	    String generatedPassword = PasswordGenerator.generatePassword(12);
	    System.err.println(generatedPassword + " generatedPassword");

	    Admin admin = new Admin();
	    admin.setFullName(adminDto.getFullName());
	    admin.setEmail(adminDto.getEmail());
	    admin.setMobileNumber(adminDto.getMobileNumber());
	    admin.setPassword(bCryptPasswordEncoder.encode(generatedPassword));
	    admin.setSuperAdmin(superAdmin);
	    admin.setRole("admin");
	    admin.setCreatedDate(LocalDate.now());
	   // System.out.println(mobileNumber);

	    Admin savedAdmin = adminRepository.save(admin);
  
	    sendEmailToAdmin(adminDto.getEmail(), generatedPassword);

	
	    AdminDto savedAdminDto = mapToAdminDto(savedAdmin);

	    return savedAdminDto;
	}
	private AdminDto mapToAdminDto(Admin admin) {
		AdminDto adminDto = new AdminDto();
		adminDto.setAdmin_Id(admin.getAdmin_Id());
		adminDto.setFullName(admin.getFullName());
		adminDto.setEmail(admin.getEmail());
		adminDto.setMobileNumber(admin.getMobileNumber());
		adminDto.setRole(admin.getRole());
		admin.setCreatedDate(LocalDate.now());
		// Set any other fields as necessary
		return adminDto;
	}

	private void sendEmailToAdmin(String email, String password) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Your Admin Account Details");
		message.setText("Hello,\n\nYour admin account has been created successfully.\n\n" + "Username: " + email + "\n"
				+ "Password: " + password + "\n\n"
				+ "For security reasons, please change your password upon your first login.\n"
				+ "You can reset your password here: " + changePasswordUrl + "\n\n" + "Best Regards,\nTechpixe");

		mailSender.send(message);
	}

	@Override
	public ResponseEntity<?> loginByMobileNumber(Long mobileNumber, String password) {
		Admin admin = adminRepository.findByMobileNumber(mobileNumber);

		if (admin != null && bCryptPasswordEncoder.matches(password, admin.getPassword())) {
			AdminDto AdminDto = new AdminDto();
			AdminDto.setAdmin_Id(admin.getAdmin_Id());
			AdminDto.setFullName(admin.getFullName());
			AdminDto.setEmail(admin.getEmail());
			AdminDto.setMobileNumber(admin.getMobileNumber());
			AdminDto.setRole(admin.getRole());
			return ResponseEntity.ok(AdminDto);
		} else {
			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setError("Invalid mobile number or password");
			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}

	@Override
	public ResponseEntity<?> loginByEmail(String email, String password) {
		Admin admin = adminRepository.findByEmail(email);
		System.out.println(email);

		if (admin != null && bCryptPasswordEncoder.matches(password, admin.getPassword())) {
			AdminDto adminDto = new AdminDto();
			adminDto.setAdmin_Id(admin.getAdmin_Id());
			adminDto.setFullName(admin.getFullName());
			adminDto.setEmail(admin.getEmail());
			adminDto.setMobileNumber(admin.getMobileNumber());
			// adminDto.setPassword(admin.getPassword());
			adminDto.setRole(admin.getRole());
			return ResponseEntity.ok(adminDto);

		} else {
			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setError("Invalid email or password");
			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}

	// ***************CHANGE PASSWORD*************************

	@Override
	@Transactional 
	public ResponseEntity<?> changePassword(Long admin_Id, String password, String confirmPassword) {
	    Admin admin = adminRepository.findById(admin_Id)
	            .orElseThrow(() -> new RuntimeException("Super Admin not found: " + admin_Id));

	    if (!password.equals(confirmPassword)) {
	        return ResponseEntity.badRequest().body("Password and confirm password do not match.");
	    }

	    String encodedPassword = bCryptPasswordEncoder.encode(confirmPassword);
	    System.err.println("Encoded Password: " + encodedPassword);

	    admin.setPassword(encodedPassword);

	    adminRepository.save(admin);

	    System.err.println("Updated Password: " + admin.getPassword());

	    // Return a success message
	    return ResponseEntity.ok("Password changed successfully.");
	}

	@Override
	public ResponseEntity<?> processForgotPassword(String email) { // Changed method name for clarity
		Admin admin = adminRepository.findByEmail(email);

		if (admin != null) {
			String password = PasswordGenerator.generatePassword(12);
			admin.setPassword(bCryptPasswordEncoder.encode(password));
			adminRepository.save(admin);

			// Prepare email
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(fromMail);
			simpleMailMessage.setTo(email);
			simpleMailMessage.setSubject("Password Reset Successfully in IT Portfolio Application");
			simpleMailMessage.setText("Hello,\n\nYour admin account has been created successfully.\n\n" + "Username: "
					+ email + "\n" + "Password: " + password + "\n\n"
					+ "For security reasons, please change your password upon your first login.\n"
					+ "You can reset your password here: " + changePasswordUrl + "\n\n" + "Best Regards,\nTechpixe");
			mailSender.send(simpleMailMessage);

			AdminDto adminDto = new AdminDto();
			adminDto.setAdmin_Id(admin.getAdmin_Id());
			adminDto.setFullName(admin.getFullName());
			adminDto.setEmail(email);
			adminDto.setMobileNumber(admin.getMobileNumber());
			adminDto.setRole(admin.getRole());

			return ResponseEntity.ok(adminDto);
		} else {
			ErrorResponseDto errorResponseDto = new ErrorResponseDto();
			errorResponseDto.setError("Email not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
		}
	}

	public List<AdminDto> getAllAdmins() {
		return adminRepository.findAll().stream().map(admin -> {
			AdminDto adminDto = new AdminDto();
			adminDto.setAdmin_Id(admin.getAdmin_Id());
			adminDto.setFullName(admin.getFullName());
			adminDto.setEmail(admin.getEmail());
			adminDto.setMobileNumber(admin.getMobileNumber());
			adminDto.setRole(admin.getRole());
			return adminDto;
		}).collect(Collectors.toList());
	}

//	@Override
//	public Optional<Admin> getAdminsById(Long superAdminId) {
//		return adminRepository.findById(superAdminId) ;
//	}
	public List<AdminDto> getAdminsBySuperAdminId(Long superAdminId) {
		return adminRepository.findBySuperAdminId(superAdminId).stream().map(admin -> {
			AdminDto adminDto = new AdminDto();
			adminDto.setAdmin_Id(admin.getAdmin_Id());
			adminDto.setFullName(admin.getFullName());
			adminDto.setEmail(admin.getEmail());
			adminDto.setMobileNumber(admin.getMobileNumber());
			adminDto.setRole(admin.getRole());
			return adminDto;
		}).collect(Collectors.toList());
	}

	@Override
	public AdminDto updateAdmin(Long id, AdminDto adminDetails) {
		Admin admin = adminRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User is not present: " + id));

		// Update admin fields
		admin.setFullName(adminDetails.getFullName());
		admin.setEmail(adminDetails.getEmail());
		admin.setMobileNumber(adminDetails.getMobileNumber());
		admin.setRole("admin");

		// Save the updated admin back to the database
		Admin updatedAdmin = adminRepository.save(admin);

		// Convert Admin to AdminDto before returning
		return fromAdmin(updatedAdmin); // Use a method like fromAdmin to convert
	}

	public static AdminDto fromAdmin(Admin admin) {
		AdminDto adminDto = new AdminDto();
		adminDto.setAdmin_Id(admin.getAdmin_Id());
		adminDto.setFullName(admin.getFullName());
		adminDto.setEmail(admin.getEmail());
		adminDto.setMobileNumber(admin.getMobileNumber());
		adminDto.setRole(admin.getRole());
		return adminDto;
	}

	public Optional<AdminDto> getAdminById(Long adminId) {
		return adminRepository.findById(adminId).map(admin -> {
			AdminDto adminDto = new AdminDto();
			adminDto.setAdmin_Id(admin.getAdmin_Id());
			adminDto.setFullName(admin.getFullName());
			adminDto.setEmail(admin.getEmail());
			adminDto.setMobileNumber(admin.getMobileNumber());
			adminDto.setRole(admin.getRole());
			return adminDto;
		});
	}

	public void deleteAdminById(Long id) {
		if (adminRepository.existsById(id)) {
			adminRepository.deleteById(id);
		} else {
			throw new RuntimeException("Admin not found with id: " + id);
		}
	}
}
