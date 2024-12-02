package com.techpixe.website.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleId;

	@Column(nullable = false)
	private String roleName;

	@Column(nullable = true)
	private String username;

	@Column(nullable = true)
	private String password;
	
	@Column(nullable = true)
	private String roleUrl;  // New field to store the URL associated with the role
	
    @ManyToOne()
	@JoinColumn(name = "product_id")
	@JsonBackReference
	private Product product;
}
