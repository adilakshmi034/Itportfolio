package com.techpixe.website.dto;

import lombok.Data;

@Data
public class SuperAdminDto {
	private Long id;
	private String fullName;
	private String email;
	private Long mobileNumber;
	private String role;

}
