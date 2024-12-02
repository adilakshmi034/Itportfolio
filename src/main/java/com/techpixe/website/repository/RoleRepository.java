package com.techpixe.website.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techpixe.website.entity.Product;
import com.techpixe.website.entity.Role;

public interface RoleRepository extends JpaRepository<Role,  Long>{


	 @Query("SELECT r FROM Role r WHERE r.product = :product AND r.roleName = :roleName")
	    Optional<Role> findByProductAndRoleName(@Param("product") Product product, @Param("roleName") String roleName);

	 @Query("SELECT r FROM Role r WHERE r.product.productId = :productId")
	List<Role> findByProduct_ProductId(Long productId);

}
