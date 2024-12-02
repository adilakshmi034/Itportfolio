package com.techpixe.website.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.techpixe.website.constants.PasswordGenerator;
import com.techpixe.website.dto.ErrorResponseDto;
import com.techpixe.website.dto.UserDto;
import com.techpixe.website.entity.User;
import com.techpixe.website.repository.SuperAdminRepository;
import com.techpixe.website.repository.UserRepository;
import com.techpixe.website.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	SuperAdminRepository superAdminRepository;

//	@Autowired
//	private AdminRepository adminRepository;

//	@Autowired
//	private SalesRepository salesRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Value("$(spring.mail.username)")
	private String fromMail;

	public User createUser(Long superAdminId, String fullName, String email, Long mobileNumber) {
//        Sales sales = salesRepository.findById(sales_Id)
//                .orElseThrow(() -> new RuntimeException("sale not found with id " + sales_Id));

//		SuperAdmin superAdmin = superAdminRepository.findById(superAdminId)
//				.orElseThrow(() -> new RuntimeException("sale not found with id " + superAdminId));

		String generatedPassword = PasswordGenerator.generatePassword(12); // Adjust the length as needed

		User users = new User();
		users.setFullName(fullName);
		users.setEmail(email);
		users.setMobileNumber(mobileNumber);
		users.setPassword(generatedPassword);
		//users.setSuperAdmin(superAdmin);
		// users.setSales(sales);

		User savedUsers = userRepository.save(users);

		sendEmailToAdmin(email, generatedPassword);

		return savedUsers;
	}

	private void sendEmailToAdmin(String email, String password) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Your Admin Account Details");
		message.setText("Hello,\n\nYour admin account has been created successfully.\n\nUsername: " + email
				+ "\nPassword: " + password + "\n\nBest Regards,\nYour Company");

		mailSender.send(message);
	}

	@Override
	public ResponseEntity<?> loginByMobileNumber(Long mobileNumber, String password) {
		User users = userRepository.findByMobileNumber(mobileNumber);

		if (users != null && users.getPassword().equals(password)) {
			UserDto userDto = new UserDto();
			userDto.setUser_Id(users.getUserId());
			userDto.setFullName(users.getFullName());
			userDto.setEmail(users.getEmail());
			userDto.setMobileNumber(users.getMobileNumber());
			userDto.setPassword(users.getPassword());
			return ResponseEntity.ok(userDto);
		} else {
			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setError("Invalid mobile number or password");
			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}

	@Override
	public ResponseEntity<?> loginByEmail(String email, String password) {
		User users = userRepository.findByEmail(email);
		System.out.println(email);

		if (users != null && users.getPassword().equals(password)) {
			UserDto userDto = new UserDto();
			userDto.setUser_Id(users.getUserId());
			userDto.setFullName(users.getFullName());
			userDto.setEmail(users.getEmail());
			userDto.setMobileNumber(users.getMobileNumber());
			userDto.setPassword(users.getPassword());
			return ResponseEntity.ok(userDto);
		} else {
			ErrorResponseDto errorResponse = new ErrorResponseDto();
			errorResponse.setError("Invalid email or password");
			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}

	// ***************CHANGE PASSWORD*************************

	@Override
	public ResponseEntity<?> changePassword(Long userId, String password, String confirmPassword) {
		User users = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("sale is not present: " + userId));

		if (users != null && users.getPassword().equals(password)) {

			System.out.println("user Password is Successfully Changed");

			UserDto userDto = new UserDto();
			userDto.setUser_Id(users.getUserId());
			userDto.setFullName(users.getFullName());
			userDto.setEmail(users.getEmail());
			userDto.setMobileNumber(users.getMobileNumber());
			userDto.setPassword(confirmPassword);

			users.setPassword(confirmPassword);
			userRepository.save(users);
			return ResponseEntity.ok(userDto);

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
		User users = userRepository.findByEmail(email);

		if (users != null) {

			System.out.println("User : Password will be sent to E-Mail Number");

			UserDto userDto = new UserDto();
			userDto.setUser_Id(users.getUserId());
			userDto.setFullName(users.getFullName());
			userDto.setEmail(email);
			userDto.setMobileNumber(users.getMobileNumber());
			userDto.setPassword(users.getPassword());

			String password = PasswordGenerator.generatePassword(12);
			userDto.setPassword(password);

			users.setPassword(password);
			userRepository.save(users);

			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(fromMail);
			simpleMailMessage.setTo(email);
			simpleMailMessage.setSubject("password changed Successfully in E_HR application\n");
			simpleMailMessage.setText("Dear " + users.getFullName()
					+ "\n\nPlease check your  email and generated password\n UserEmail  :" + email
					+ "\n  MobileNumber :" + users.getMobileNumber() + "\n New Password   :" + password + "\n\n"
					+ "you will be required to reset the New password upon login\n\n\n if you have any question or if you would like to request a call-back,please email us at support info@techpixe.com");
			mailSender.send(simpleMailMessage);

			return ResponseEntity.ok(userDto);
		} else {

			System.out.println("****Invalid Email****");

			ErrorResponseDto errorResponseDto = new ErrorResponseDto();
			errorResponseDto.setError("Email is not matching");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
		}
	}

	@Override
	public List<User> getAllAdmins() {
		return userRepository.findAll();
	}
	
//	 public List<User> getUsersBySuperAdminId(Long superAdminId) {
//	        return userRepository.findBySuperAdmin_SuperAdminId(superAdminId);
//	    }

}
