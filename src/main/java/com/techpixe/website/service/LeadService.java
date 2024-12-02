package com.techpixe.website.service;

import java.util.List;
import java.util.Optional;

import com.techpixe.website.constants.SalesStatus;
import com.techpixe.website.dto.LeadsDto;
import com.techpixe.website.entity.Leads;

public interface LeadService {

	Leads createLead(LeadsDto leadsDto, Long salesId);

	List<Leads> getLeadsBySalesId(Long salesId);

	Leads createLeadBySuperAdmin(LeadsDto leadDTO, Long superAdminId);

	//List<Leads> getLeadsBySuperAdminId(Long superAdminId);

	void deleteLeadById(Long leadId);

	Leads updateLeadById(Long id, LeadsDto updatedLead);

	List<Leads> getLeadsExcludingDefaultStatus(SalesStatus leadGeneration);

	List<Leads> findAll();

	Optional<Leads> getLeadById(Long id);

}
