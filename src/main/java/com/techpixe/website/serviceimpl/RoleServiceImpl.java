package com.techpixe.website.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techpixe.website.dto.RoleDto;
import com.techpixe.website.entity.Product;
import com.techpixe.website.entity.Role;
import com.techpixe.website.repository.ProductRepository;
import com.techpixe.website.repository.RoleRepository;
import com.techpixe.website.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ProductRepository productRepository;

	 
	@Override
	public List<Role> addRoles(Long productId, List<RoleDto> roles) {
	    // Fetch the product entity based on the provided productId
	    Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

	    List<Role> savedRoles = new ArrayList<>();

	    for (RoleDto roleDTO : roles) {
	        // Check if a role with the same name already exists for this product
	        Optional<Role> existingRole = roleRepository.findByProductAndRoleName(product, roleDTO.getRoleName());

	        if (existingRole.isPresent()) {
	            // Skip saving if the role already exists for this product
	            System.out.println("Duplicate role detected for product " + productId + " and role name " + roleDTO.getRoleName());
	            continue;
	        }

	        // Create a new Role entity from the DTO
	        Role role = new Role();
	        role.setProduct(product);
	        role.setRoleName(roleDTO.getRoleName());
	        role.setUsername(roleDTO.getUsername());
	        role.setPassword(roleDTO.getPassword());
	        role.setRoleUrl(roleDTO.getRoleUrl());

	        // Save each unique role for this product in the database
	        Role savedRole = roleRepository.save(role);
	        savedRoles.add(savedRole);
	    }

	    return savedRoles;
	}


		 public Role updateRole(Long roleId, RoleDto roleDetails) {
		        Role existingRole = roleRepository.findById(roleId)
		                .orElseThrow(() -> new RuntimeException("Role not found with id " + roleId));

		        existingRole.setRoleName(roleDetails.getRoleName());
		        existingRole.setUsername(roleDetails.getUsername());
		        existingRole.setPassword(roleDetails.getPassword());
		        existingRole.setRoleUrl(roleDetails.getRoleUrl());
		       //existingRole.setProduct(roleDetails.getProduct());

		        return roleRepository.save(existingRole);
		    }

		 public Role deleteRole(Long roleId) {
			    Role role = roleRepository.findById(roleId)
			            .orElseThrow(() -> new RuntimeException("Role not found with id " + roleId));
			    roleRepository.delete(role); // Perform deletion
			    return role; // Return the deleted role
			}
		 public Role getRoleById(Long roleId) {
		        return roleRepository.findById(roleId)
		                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
		    }
		 public List<Role> getRolesByProductId(Long productId) {
		        return roleRepository.findByProduct_ProductId(productId);
		    }

}
