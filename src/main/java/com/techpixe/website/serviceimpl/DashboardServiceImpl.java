package com.techpixe.website.serviceimpl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techpixe.website.entity.Dashboard;
import com.techpixe.website.entity.SalesDashboard;
import com.techpixe.website.repository.AdminRepository;
import com.techpixe.website.repository.SalesRepository;
import com.techpixe.website.service.DashboardService;
import com.techpixe.website.service.LeadService;
import com.techpixe.website.service.ProductService;

@Service
public class DashboardServiceImpl implements DashboardService {
    
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
    private  SalesRepository salesRepository;
	@Autowired
    private  LeadService leadService;
	
	@Autowired
	ProductService productService;

	@Override
	public List<Dashboard> getYearlyRecords(int year) {
	    List<Dashboard> monthlyRecords = new ArrayList<>();

	    // Loop through each month (1 to 12)
	    for (int month = 1; month <= 12; month++) {
	        YearMonth selectedMonth = YearMonth.of(year, month);
	        LocalDate startOfMonth = selectedMonth.atDay(1);
	        LocalDate endOfMonth = selectedMonth.atEndOfMonth();

	        long adminCount = adminRepository.findAll().stream()
	                .filter(admin -> admin.getCreatedDate().isAfter(startOfMonth.minusDays(1))
	                        && admin.getCreatedDate().isBefore(endOfMonth.plusDays(1)))
	                .count();

	        // Get the count of Sales records for the current month
	        long salesCount = salesRepository.findAll().stream()
	            .filter(sale -> sale.getCreatedDate().isAfter(startOfMonth.minusDays(1))
	                && sale.getCreatedDate().isBefore(endOfMonth.plusDays(1)))
	            .count();

	        // Get the count of Leads created for the current month
	        long leadsCount = leadService.findAll().stream()
	            .filter(lead -> lead.getCreatedDate().isAfter(startOfMonth.minusDays(1))
	                && lead.getCreatedDate().isBefore(endOfMonth.plusDays(1)))
	            .count();

	        // Get the count of Products created for the current month
	        long productCount = productService.getAllProducts().stream()
	            .filter(product -> product.getCreatedAt().isAfter(startOfMonth.minusDays(1))
	                && product.getCreatedAt().isBefore(endOfMonth.plusDays(1)))
	            .count();

	        // Add the Dashboard object for this month
	        monthlyRecords.add(new Dashboard(adminCount, salesCount, leadsCount, productCount, month));

	    }

	    // Return the list of monthly Dashboard objects
	    return monthlyRecords;
	}

	@Override
	public List<SalesDashboard> getSalesDashboatrd(Long salesId, int year) {
		 List<SalesDashboard> salesRecords = new ArrayList<>();

		    // Loop through each month (1 to 12)
		 
		    for (int month = 1; month <= 12; month++) {
		        YearMonth selectedMonth = YearMonth.of(year, month);
		        LocalDate startOfMonth = selectedMonth.atDay(1);
		        LocalDate endOfMonth = selectedMonth.atEndOfMonth();

		        // Get the count of Leads created for the current month
		        long leadsCount = leadService.findAll().stream()
		            .filter(lead -> lead.getCreatedDate().isAfter(startOfMonth.minusDays(1))
		                && lead.getCreatedDate().isBefore(endOfMonth.plusDays(1)))
		            .count();

		        // Get the count of Products created for the current month
		        long productCount = productService.getAllProducts().stream()
		            .filter(product -> product.getCreatedAt().isAfter(startOfMonth.minusDays(1))
		                && product.getCreatedAt().isBefore(endOfMonth.plusDays(1)))
		            .count();

		        // Add the Dashboard object for this month
		        salesRecords.add(new SalesDashboard( leadsCount, productCount, month));

		    }

		    // Return the list of monthly Dashboard objects
		    return salesRecords;
	}


}

