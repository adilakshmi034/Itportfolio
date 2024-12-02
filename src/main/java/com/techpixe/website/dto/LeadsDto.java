package com.techpixe.website.dto;

import java.time.LocalDate;

import com.techpixe.website.constants.SalesStatus;

import lombok.Data;

@Data
public class LeadsDto {
	private Long LeadId;
	private String fullName;
	private String email;
	private Long mobileNumber;
	private String service;
	private SalesStatus status;
	private String comment;
	private LocalDate createdDate;

}
