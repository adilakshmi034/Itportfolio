package com.techpixe.website.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboard {
	private long salesCount;
    private long leadsCount;
    private long productCount;
    private int month;

}
