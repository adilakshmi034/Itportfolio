package com.techpixe.website.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Admin {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "admin_Id")
	    private Long admin_Id;

	    @Column(name = "fullName", nullable = false)
	    @Pattern(regexp = "^[A-Za-z]+$", message = "Full name can only contain alphabets")
	    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
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
	    @NotNull(message = "Password is required")
	    @Size(min = 10, max = 10, message = "Mobile number must be exactly 10 digits.")
	    @Pattern(regexp = "(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).{8,}", 
	             message = "Password must contain at least one uppercase letter, one lowercase letter, and one number")
	    private String password;

	    @Column(name = "role")
	    //@Pattern(regexp = "^(ADMIN|SUPER_ADMIN)$", message = "Role must be either 'ADMIN' or 'SUPER_ADMIN'")
	    private String role;

	    @Column(name = "createdDate", updatable = false)
	    @PastOrPresent(message = "Created date cannot be in the future")
	    private LocalDate createdDate;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "superAdmin_Id", nullable = false)  // assuming superAdmin is required for an Admin
	    @JsonBackReference
	    @NotNull(message = "SuperAdmin must be specified")
	    private SuperAdmin superAdmin;

	    @JsonManagedReference
	    @OneToMany(mappedBy = "admin", fetch = FetchType.EAGER)
	    private List<Sales> sales = new ArrayList<>();

}
