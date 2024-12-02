package com.techpixe.website.serviceimpl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techpixe.website.entity.AdminDashboard;
import com.techpixe.website.repository.SalesRepository;
import com.techpixe.website.service.AdminDashboardService;
import com.techpixe.website.service.LeadService;
import com.techpixe.website.service.ProductService;
import com.techpixe.website.service.SaleService;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService{
	
//	@Autowired
//    private AdminRepository adminRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private LeadService leadService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private SaleService saleService;


	@Override
	public List<AdminDashboard> getAdminDashboatrd(Long adminId, int year) {
        List<AdminDashboard> monthlyRecords = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            YearMonth selectedMonth = YearMonth.of(year, month);
            LocalDate startOfMonth = selectedMonth.atDay(1);
            LocalDate endOfMonth = selectedMonth.atEndOfMonth();
            
          
            salesRepository.findAll().stream()
            .filter(sale -> sale.getAdmin() == null)
            .forEach(sale -> System.err.println("Sale with ID " + sale.getSales_Id() + " has null Admin "+sale.getAdmin()));


           
            long salesCount = saleService.getSalesByAdminId(adminId).stream()
                .filter(sale -> sale.getAdmin().getAdmin_Id().equals(adminId)
                        && sale.getCreatedDate().isAfter(startOfMonth.minusDays(1))
                        && sale.getCreatedDate().isBefore(endOfMonth.plusDays(1)))
                .count();
            
            
        

          
            long leadsCount = leadService.findAll().stream()
    	            .filter(lead -> lead.getCreatedDate().isAfter(startOfMonth.minusDays(1))
    	                && lead.getCreatedDate().isBefore(endOfMonth.plusDays(1)))
    	            .count();

           
            long productCount = productService.getAllProducts().stream()
    	            .filter(product -> product.getCreatedAt().isAfter(startOfMonth.minusDays(1))
    	                && product.getCreatedAt().isBefore(endOfMonth.plusDays(1)))
    	            .count();

            monthlyRecords.add(new AdminDashboard(salesCount, leadsCount, productCount, month));
        }

        return monthlyRecords;
		
	}
}

	
	


