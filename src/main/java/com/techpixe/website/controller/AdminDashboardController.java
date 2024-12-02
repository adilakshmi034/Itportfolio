package com.techpixe.website.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.website.entity.AdminDashboard;
import com.techpixe.website.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admindashboard")
public class AdminDashboardController {
	
	  @Autowired
	  private AdminDashboardService adminDashboardService;

	  @GetMapping("/{adminId}")
	  public ResponseEntity<List<AdminDashboard>> getAdminDashboard(@PathVariable Long adminId,@RequestParam int year){
		  List<AdminDashboard> dashboardData=adminDashboardService.getAdminDashboatrd(adminId,year);
		return ResponseEntity.ok(dashboardData);
		  
	  }
	
	
	

}
