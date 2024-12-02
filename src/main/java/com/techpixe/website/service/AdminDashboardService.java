package com.techpixe.website.service;

import java.util.List;

import com.techpixe.website.entity.AdminDashboard;

public interface AdminDashboardService {

	List<AdminDashboard> getAdminDashboatrd(Long adminId, int year);

}
