package com.techpixe.website.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userId")
	private Long userId;

	@Column(name = "fullName", nullable = false)
	@Pattern(regexp = "^[A-Za-z]+$", message = "Full name can only contain alphabets")
	private String fullName;

	@Column(name = "email", unique = true, nullable = false)
	@Email(message = "Please provide a valid email address")
	@Pattern(regexp = ".+@.+\\..+", message = "Email address must contain @ symbol")

	private String email;

	@Column(name = "mobileNumber", unique = true, nullable = false)
	@NotNull(message = "Mobile number is required")
	@Digits(integer = 15, fraction = 0, message = "Mobile number should only contain numeric digits")
	private Long mobileNumber;

	@Column(name = "password", nullable = false)
	private String password;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "superAdmin_Id")
//	@JsonManagedReference
//	private SuperAdmin superAdmin;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "sales_Id")
//	@JsonBackReference
//	private Sales sales ;

}
