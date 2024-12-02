package com.techpixe.website.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techpixe.website.constants.PasswordGenerator;
import com.techpixe.website.dto.AdminDto;
import com.techpixe.website.dto.ErrorResponseDto;
import com.techpixe.website.dto.SalesDto;
import com.techpixe.website.dto.SuperAdminDto;
import com.techpixe.website.dto.UserDto;
import com.techpixe.website.entity.Admin;
import com.techpixe.website.entity.Sales;
import com.techpixe.website.entity.SuperAdmin;
import com.techpixe.website.entity.User;
import com.techpixe.website.repository.AdminRepository;
import com.techpixe.website.repository.SalesRepository;
import com.techpixe.website.repository.SuperAdminRepository;
import com.techpixe.website.repository.UserRepository;
import com.techpixe.website.service.SuperAdminService;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	SalesRepository salesRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	SuperAdminRepository superAdminRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Value("$(spring.mail.username)")
	private String fromMail;

	@Autowired
	private BCryptPasswordEncoder byBCryptPasswordEncoder;

	@Override
	public SuperAdmin registerAdmin(String fullName, String email, Long mobileNumber, String password) {

		SuperAdmin admin = new SuperAdmin();
		admin.setFullName(fullName);
		admin.setEmail(email);
		admin.setMobileNumber(mobileNumber);
		admin.setPassword(password);
		admin.setRole("superadmin");
		return superAdminRepository.save(admin);
	}

	@Override
	public ResponseEntity<?> loginByMobileNumber(Long mobileNumber, String password) {
		password = password.trim();

		Admin user = adminRepository.findByMobileNumber(mobileNumber);
		Sales user1 = salesRepository.findByMobileNumber(mobileNumber);
		User user2 = userRepository.findByMobileNumber(mobileNumber);
		SuperAdmin superAdmin = superAdminRepository.findByMobileNumber(mobileNumber);

		if (superAdmin != null && byBCryptPasswordEncoder.matches(password, superAdmin.getPassword())) {

			SuperAdminDto applicationFormDTo = new SuperAdminDto();
			applicationFormDTo.setId(superAdmin.getId());
			applicationFormDTo.setFullName(superAdmin.getFullName());
			applicationFormDTo.setEmail(superAdmin.getEmail());
			applicationFormDTo.setMobileNumber(superAdmin.getMobileNumber());
			applicationFormDTo.setRole(superAdmin.getRole());

			return ResponseEntity.ok(applicationFormDTo);
		} else if (user != null && byBCryptPasswordEncoder.matches(password, user.getPassword())) {

			AdminDto AdminDto = new AdminDto();
			AdminDto.setAdmin_Id(user.getAdmin_Id());
			AdminDto.setFullName(user.getFullName());
			AdminDto.setEmail(user.getEmail());
			AdminDto.setMobileNumber(user.getMobileNumber());
			// AdminDto.setPassword(user1.getPassword());
			AdminDto.setRole("Sales");

			return ResponseEntity.ok(AdminDto);
		} else if (user1 != null && byBCryptPasswordEncoder.matches(password, user1.getPassword())) {

			SalesDto SalesDto = new SalesDto();
			SalesDto.setSales_Id(user1.getSales_Id());
			SalesDto.setFullName(user1.getFullName());
			SalesDto.setEmail(user1.getEmail());
			SalesDto.setMobileNumber(user1.getMobileNumber());
			SalesDto.setRole("Sales");

			return ResponseEntity.ok(SalesDto);
		} else if (user2 != null && byBCryptPasswordEncoder.matches(password, user2.getPassword())) {

			UserDto userDto = new UserDto();
			userDto.setUser_Id(user2.getUserId());
			userDto.setFullName(user1.getFullName());
			userDto.setEmail(user1.getEmail());
			userDto.setMobileNumber(user1.getMobileNumber());
			userDto.setPassword(user1.getPassword());

			return ResponseEntity.ok(userDto);
		}

		else {

			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setError("Invalid mobile number or password");
			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}

	@Override
	public ResponseEntity<?> loginByEmail(String email, String password) {
		password = password.trim();
		email = email.trim();
		SuperAdmin superAdmin = superAdminRepository.findByEmail(email);
		// System.out.println(superAdmin);
		Admin user = adminRepository.findByEmail(email);
		System.out.println(user);
		Sales user1 = salesRepository.findByEmail(email);
		User user2 = userRepository.findByEmail(email);

		if (superAdmin != null && byBCryptPasswordEncoder.matches(password, superAdmin.getPassword())) {

			SuperAdminDto applicationFormDTo = new SuperAdminDto();
			applicationFormDTo.setId(superAdmin.getId());
			applicationFormDTo.setFullName(superAdmin.getFullName());
			applicationFormDTo.setEmail(superAdmin.getEmail());
			applicationFormDTo.setMobileNumber(superAdmin.getMobileNumber());
			applicationFormDTo.setRole("superadmin");

			return ResponseEntity.ok(applicationFormDTo);

		} else if (user != null && byBCryptPasswordEncoder.matches(password, user.getPassword())) {

			AdminDto AdminDto = new AdminDto();
			AdminDto.setAdmin_Id(user.getAdmin_Id());
			AdminDto.setFullName(user.getFullName());
			AdminDto.setEmail(user.getEmail());
			AdminDto.setMobileNumber(user.getMobileNumber());
			// AdminDto.setPassword(user.getPassword());
			AdminDto.setRole("admin");

			return ResponseEntity.ok(AdminDto);
		} else if (user1 != null && byBCryptPasswordEncoder.matches(password, user1.getPassword())) {

			SalesDto SalesDto = new SalesDto();
			SalesDto.setSales_Id(user1.getSales_Id());
			SalesDto.setFullName(user1.getFullName());
			SalesDto.setEmail(user1.getEmail());
			SalesDto.setMobileNumber(user1.getMobileNumber());
			SalesDto.setRole("Sales");

			return ResponseEntity.ok(SalesDto);
		} else if (user2 != null && user2.getPassword().equals(password)) {

			UserDto userDto = new UserDto();
			userDto.setUser_Id(user2.getUserId());
			userDto.setFullName(user2.getFullName());
			userDto.setEmail(user2.getEmail());
			userDto.setMobileNumber(user2.getMobileNumber());
			userDto.setPassword(user2.getPassword());

			return ResponseEntity.ok(userDto);
		}

		else {

			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setError("Invalid email number or password");
			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}

	@Override
	@Transactional 
	public ResponseEntity<?> changePassword(Long id, String password, String confirmPassword) {
	    SuperAdmin admin = superAdminRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Super Admin not found: " + id));

	    if (!password.equals(confirmPassword)) {
	        return ResponseEntity.badRequest().body("Password and confirm password do not match.");
	    }

	    String encodedPassword = byBCryptPasswordEncoder.encode(confirmPassword);
	    System.err.println("Encoded Password: " + encodedPassword);

	    admin.setPassword(encodedPassword);

	    superAdminRepository.save(admin);

	    System.err.println("Updated Password: " + admin.getPassword());

	    // Return a success message
	    return ResponseEntity.ok("Password changed successfully.");
	}


	@Override
	public ResponseEntity<?> forgotPassword(String email) {
		SuperAdmin admin = superAdminRepository.findByEmail(email);

		if (admin != null) {

			System.out.println("User : Password will be sent to E-Mail Number");

			SuperAdminDto adminDto = new SuperAdminDto();
			adminDto.setId(admin.getId());
			adminDto.setFullName(admin.getFullName());
			adminDto.setEmail(email);
			adminDto.setMobileNumber(admin.getMobileNumber());

			String password = PasswordGenerator.generatePassword(12);

			admin.setPassword(byBCryptPasswordEncoder.encode(password));
			superAdminRepository.save(admin);

			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(fromMail);
			simpleMailMessage.setTo(email);
			simpleMailMessage.setSubject("password changed Successfully in IT portfolio application\n");
			simpleMailMessage.setText("Dear " + admin.getFullName()
					+ "\n\nPlease check your  email and generated password\n UserEmail  :" + email
					+ "\n  MobileNumber :" + admin.getMobileNumber() + "\n New Password   :" + password + "\n\n"
					+ "you will be required to reset the New password upon login\n\n\n if you have any question or if you would like to request a call-back,please email us at support info@techpixe.com");
			mailSender.send(simpleMailMessage);

			return ResponseEntity.ok(adminDto);
		} else {

			System.out.println("****Invalid Email****");

			ErrorResponseDto errorResponseDto = new ErrorResponseDto();
			errorResponseDto.setError("Email is not matching");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
		}
	}

}
