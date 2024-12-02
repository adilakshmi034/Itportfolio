package com.techpixe.website.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sales {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sales_Id")
	private Long sales_Id;

	@Column(name = "fullName", nullable = false)
	@Pattern(regexp = "^[A-Za-z]+$", message = "Full name can only contain alphabets")
	private String fullName;

	@Column(name = "email", unique = true, nullable = false)
	@Email(message = "Please provide a valid email address")
	@Pattern(regexp = ".+@.+\\..+", message = "Email address must contain @ symbol")

	private String email;

	@Column(name = "mobileNumber", unique = true, nullable = false)
	@NotNull(message = "Mobile number is required")
	@Digits(integer = 10, fraction = 0, message = "Mobile number should only contain numeric digits")
	@Size(min = 0, max = 10, message = "Mobile number should only contain numeric digits")
	private Long mobileNumber;

	@Column(name = "password", nullable = false)
	@JsonIgnore
	private String password;

	private String role;

	@CreationTimestamp
	@Column(name = "createdDate", updatable = false)
	private LocalDate createdDate;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "admin_Id")
	@JsonBackReference
	private Admin admin;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "superAdmin_Id")
	@JsonBackReference
	private SuperAdmin superAdmin;

	@JsonManagedReference
	@OneToMany(mappedBy = "sales", fetch = FetchType.EAGER,orphanRemoval = true)
	private List<Leads> leads = new ArrayList<>();

}
