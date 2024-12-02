package com.techpixe.website.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProductDto {

	private Long productId;
	private String productName;
	private String productDescription;
	private double price;
	private double discountPrice;
	private Long rating;
	private LocalDate createdAt;
	private byte[] image;
	private String base64Image;

}
