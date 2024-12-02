package com.techpixe.website.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.website.dto.LeadsDto;
import com.techpixe.website.entity.Leads;
import com.techpixe.website.service.LeadService;

@RestController
@RequestMapping("/api/leads")
public class LeadsController {

	@Autowired
	private LeadService leadService;

	@PostMapping("/sales/{salesId}")
	public ResponseEntity<Leads> createLead(@PathVariable Long salesId, @RequestBody LeadsDto leadsDto) {
		Leads createdLead = leadService.createLead(leadsDto, salesId);
		return ResponseEntity.ok(createdLead);
	}

	@GetMapping("/{salesId}")
	public List<Leads> getLeadsBySalesId(@PathVariable("salesId") Long salesId) {
		return leadService.getLeadsBySalesId(salesId);
	}
	
	   @PostMapping("/create/{superAdminId}")
	    public ResponseEntity<Leads> createLead(
	            @RequestBody LeadsDto leadDTO,
	            @PathVariable Long superAdminId) {

	        Leads createdLead = leadService.createLeadBySuperAdmin(leadDTO, superAdminId);
	        return ResponseEntity.ok(createdLead);
	    }
//	   
//	   @GetMapping("/superadmin/{superAdminId}")
//	    public List<Leads> getLeadsBySuperAdminId(@PathVariable Long superAdminId) {
//	        return leadService.getLeadsBySuperAdminId(superAdminId);
//	    }
	   
	   @DeleteMapping("/{id}")
	    public ResponseEntity<String> deleteLeadById(@PathVariable("id") Long leadId) {
	        try {
	        	leadService.deleteLeadById(leadId);
	            return ResponseEntity.ok("Lead deleted successfully.");
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.status(404).body(e.getMessage());
	        }
	    }
	   
	   @PutMapping("/{id}")
	    public ResponseEntity<Leads> updateLead(@PathVariable Long id, @RequestBody LeadsDto updatedLead) {
		   System.err.println(updatedLead);
	        Leads lead = leadService.updateLeadById(id, updatedLead);
	        if (lead != null) {
	            return new ResponseEntity<>(lead, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	   
	   @GetMapping("/all")
	    public List<Leads> getAllLeads() {
	        return leadService.findAll();
	    }
	   
	   @GetMapping("/leads/{id}")
	    public ResponseEntity<Leads> getLeadById(@PathVariable Long id) {
	        Optional<Leads> lead = leadService.getLeadById(id);
	        if (lead.isPresent()) {
	            return ResponseEntity.ok(lead.get());
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
}
