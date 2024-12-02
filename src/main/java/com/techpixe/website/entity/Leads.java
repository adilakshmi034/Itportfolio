package com.techpixe.website.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techpixe.website.constants.SalesStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Leads {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LeadId")
	private Long LeadId;

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

	private String service;

	@CreationTimestamp
	@Column(name = "createdDate", updatable = false)
	private LocalDate createdDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private SalesStatus status;

	@Column(name = "comment")
	private String comment;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sales_Id")
	@JsonBackReference
	private Sales sales;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "superAdminId")
//	@JsonBackReference
//	private SuperAdmin superAdmin;

}
