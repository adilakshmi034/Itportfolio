package com.techpixe.website.service;

import java.util.List;

import com.techpixe.website.entity.Dashboard;
import com.techpixe.website.entity.SalesDashboard;

public interface DashboardService {


	List<Dashboard> getYearlyRecords(int  year);


	List<SalesDashboard> getSalesDashboatrd(Long salesId, int year);

}
