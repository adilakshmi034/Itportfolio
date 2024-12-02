package com.techpixe.website.service;

import java.util.List;

import com.techpixe.website.dto.RoleDto;
import com.techpixe.website.entity.Role;

public interface RoleService {

	//Role addRolesToProduct(Long productId,Role  roles);
//	Role addRoles(Long productId,String roleName, String username,String password);
//	Role addRoleToProduct(Long productId, Role role);
//	List<Role> createRolesForProduct(Long productId, List<Role> roles);
	List<Role> addRoles(Long productId, List<RoleDto> roles);

	Role updateRole(Long roleId, RoleDto roleDetails);

	Role deleteRole(Long roleId);

	Role getRoleById(Long roleId);

	List<Role> getRolesByProductId(Long productId);

}
