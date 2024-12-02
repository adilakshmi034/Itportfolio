package com.techpixe.website.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SuperAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullName", nullable = false)
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Full name can only contain alphabets and spaces")
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Please provide a valid email address")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$", message = "Email address must be in a valid format")
    private String email;

    @Column(name = "mobileNumber", unique = true, nullable = false)
    @NotNull(message = "Mobile number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be exactly 10 digits")
    private Long mobileNumber;

    @Column(name = "password", nullable = false)
    private String password;

    private String role;

    @JsonManagedReference
    @OneToMany(mappedBy = "superAdmin", fetch = FetchType.EAGER)
    private List<Admin> admins = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "superAdmin", fetch = FetchType.EAGER)
    private List<Sales> sales = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "superAdmin", fetch = FetchType.EAGER)
    private List<Category> categories = new ArrayList<>();
}
