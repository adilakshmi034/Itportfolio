package com.techpixe.website.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techpixe.website.constants.SalesStatus;
import com.techpixe.website.dto.LeadsDto;
import com.techpixe.website.entity.Leads;
import com.techpixe.website.entity.Sales;
import com.techpixe.website.repository.LeadsRepository;
import com.techpixe.website.repository.SalesRepository;
import com.techpixe.website.service.LeadService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LeadServiceImpl implements LeadService {

	@Autowired
	private LeadsRepository leadsRepository;

	@Autowired
	private SalesRepository salesRepository;
	
//	@Autowired
//	private SuperAdminRepository superAdminRepository;

	public Leads createLead(LeadsDto leadsDto, Long salesId) {
		// Fetch the Sales entity by salesId
		Sales sales = salesRepository.findById(salesId)
				.orElseThrow(() -> new EntityNotFoundException("Sales not found with id: " + salesId));

		// Convert LeadsDto to Leads entity
		Leads lead = new Leads();
		lead.setFullName(leadsDto.getFullName());
		lead.setEmail(leadsDto.getEmail());
		lead.setMobileNumber(leadsDto.getMobileNumber());
		lead.setService(leadsDto.getService());
		
		if (leadsDto.getStatus() == null) {
			lead.setStatus(SalesStatus.LEAD_GENERATION);  // Assuming PENDING is a default status
		} else {
			lead.setStatus(leadsDto.getStatus());
		}

		// Associate the lead with the sales entity
		lead.setSales(sales);

		// Save the lead to the database
		return leadsRepository.save(lead);
	}

	public List<Leads> getLeadsBySalesId(Long salesId) {
	    return  leadsRepository.findLeadsBySalesIdNative(salesId);
//	    return leads.stream()
//	                .map(this::convertToDto) // Convert each Leads entity to LeadsDto
//	                .toList();
	}
	@Override
	public Leads createLeadBySuperAdmin(LeadsDto leadDTO, Long superAdminId) {
		// Find the super admin by ID
//        SuperAdmin superAdmin = superAdminRepository.findById(superAdminId)
//        		.orElseThrow(() -> new EntityNotFoundException("SuperAdmin not found with id: " + superAdminId));
        // Map LeadDTO to Leads entity
        Leads newLead = new Leads();
        newLead.setFullName(leadDTO.getFullName());
        newLead.setEmail(leadDTO.getEmail());
        newLead.setMobileNumber(leadDTO.getMobileNumber());
        newLead.setService(leadDTO.getService());
      

        // Save the new lead
        return leadsRepository.save(newLead);
    
	}
	
//	 public List<Leads> getLeadsBySuperAdminId(Long superAdminId) {
//	        return leadsRepository.findLeadsBySuperAdminId(superAdminId);
//	    }
//	 
	 public void deleteLeadById(Long leadId) {
	        Optional<Leads> lead = leadsRepository.findById(leadId);
	        if (lead.isPresent()) {	
	            leadsRepository.deleteById(leadId);
	        } else {
	            throw new IllegalArgumentException("Lead not found with ID: " + leadId);
	        }
	    }


	 @Override
	 public Leads updateLeadById(Long id, LeadsDto updatedLead) {
	     Optional<Leads> optionalLead = leadsRepository.findById(id);
	     
	     if (optionalLead.isPresent()) {
	         Leads existingLead = optionalLead.get();
	         
	         // Update lead details only if the provided data is not null
	         if (updatedLead.getFullName() != null) {
	             existingLead.setFullName(updatedLead.getFullName());
	         }
	         if (updatedLead.getEmail() != null) {
	             existingLead.setEmail(updatedLead.getEmail());
	         }
	         if (updatedLead.getMobileNumber() != null) {
	             existingLead.setMobileNumber(updatedLead.getMobileNumber());
	         }
	         if (updatedLead.getService() != null) {
	             existingLead.setService(updatedLead.getService());
	         }
	         if (updatedLead.getStatus() != null) {
	             existingLead.setStatus(updatedLead.getStatus());
	         }
	         if (updatedLead.getComment() != null) {
	             existingLead.setComment(updatedLead.getComment());
	         }
	         
	         // Save the updated lead and return
	         return leadsRepository.save(existingLead);
	     }
	     
	     return null;  // Handle lead not found case
	 }
	 
	 
	 
	 
	 

	  public List<Leads> getLeadsExcludingDefaultStatus(SalesStatus defaultStatus) {
	        return leadsRepository.findByStatusNot(defaultStatus);
	    }
	  
	  
	  @Override
	    public List<Leads> findAll() {
	       return leadsRepository.findAll();
	       
	    }
	  
//	  private LeadsDto convertToDto(Leads lead) {
//		    LeadsDto dto = new LeadsDto();
//		    dto.setLeadId(lead.getLeadId()); // Map `id` from Leads to `LeadId` in LeadsDto
//		    dto.setFullName(lead.getFullName());
//		    dto.setEmail(lead.getEmail());
//		    dto.setMobileNumber(lead.getMobileNumber());
//		    dto.setService(lead.getService());
//		    dto.setStatus(lead.getStatus());
//		    dto.setComment(lead.getComment());
//		    return dto;
//		}

	  public Optional<Leads> getLeadById(Long id) {
	        return leadsRepository.findById(id);
	    }


}
