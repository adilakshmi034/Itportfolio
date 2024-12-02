package com.techpixe.website.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Category {
	
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long categoryId;

	    @Column(nullable = false, unique = true)
	    private String name;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "superadmin_id", nullable = false)
	    @JsonBackReference // Prevent recursion by ignoring the back reference to SuperAdmin
	    private SuperAdmin superAdmin;
	    
	    @OneToMany(mappedBy = "category")
	    @JsonManagedReference// To correctly serialize the products
	    private List<SubCategory> subcategories;

//	    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	    @JsonManagedReference // To correctly serialize the products
//	    private List<Product> products;

}
