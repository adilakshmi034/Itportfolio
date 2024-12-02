package com.techpixe.website.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SalesDashboard {
	private Long leadsCount;
	private Long productsCount;
	private int month;
	
}

