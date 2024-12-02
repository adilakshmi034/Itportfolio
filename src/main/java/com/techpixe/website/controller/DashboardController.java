package com.techpixe.website.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.website.entity.Dashboard;
import com.techpixe.website.entity.SalesDashboard;
import com.techpixe.website.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
	
	@Autowired
	private  DashboardService dashboardService;

	@GetMapping("/yearly-records")
	public ResponseEntity<List<Dashboard>> getYearlyRecords(@RequestParam int year) {
	    List<Dashboard> yearlyDashboard = dashboardService.getYearlyRecords(year);
	    return new ResponseEntity<>(yearlyDashboard, HttpStatus.OK);
	}
   

	  @GetMapping("/{salesId}")
	  public ResponseEntity<List<SalesDashboard>> getSalesDashboard(@PathVariable Long salesId,@RequestParam int year){
		  List<SalesDashboard> dashboardData=dashboardService.getSalesDashboatrd(salesId,year);
		return ResponseEntity.ok(dashboardData);
		  
	  }

}
