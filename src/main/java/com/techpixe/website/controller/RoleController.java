package com.techpixe.website.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.website.dto.RoleDto;
import com.techpixe.website.entity.Role;
import com.techpixe.website.service.RoleService;

@RestController
@RequestMapping("/api/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	
	@PostMapping("/savedata/{productId}")
	public ResponseEntity<List<Role>> addRolesToProduct(
	        @PathVariable Long productId,
	        @RequestBody List<RoleDto> roles) {

	    List<Role> savedRoles = roleService.addRoles(productId, roles);
	    return ResponseEntity.ok(savedRoles);
	}
	   @PutMapping("/{roleId}")
	    public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @RequestBody RoleDto roleDetails) {
	        Role updatedRole = roleService.updateRole(roleId, roleDetails);
	        return ResponseEntity.ok(updatedRole);
	    }

	   @DeleteMapping("/{roleId}")
	   public ResponseEntity<Role> deleteRole(@PathVariable Long roleId) {
	       Role deletedRole = roleService.deleteRole(roleId);
	       return ResponseEntity.ok(deletedRole); // Return the deleted role in the response
	   }
	   
	   @GetMapping("/{roleId}")
	    public Role getRoleById(@PathVariable Long roleId) {
	        return roleService.getRoleById(roleId);  // Return 404 Not Found if role not found
	    }
	   @GetMapping("/byproduct/{productId}")
	    public ResponseEntity<List<Role>> getRolesByProductId(@PathVariable Long productId) {
	        List<Role> roles = roleService.getRolesByProductId(productId);
	        if (roles.isEmpty()) {
	            return ResponseEntity.noContent().build(); // 204 No Content if no roles are found
	        }
	        return ResponseEntity.ok(roles); // 200 OK with the list of roles
	    }
	   
	   


}
