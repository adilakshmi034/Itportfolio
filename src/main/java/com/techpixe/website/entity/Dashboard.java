package com.techpixe.website.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Dashboard {
	private long adminCount;
    private long salesCount;
    private long leadsCount;
    private long productCount;
    private int month;  
	
}
	
