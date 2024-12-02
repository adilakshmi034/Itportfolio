package com.techpixe.website.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductFeature {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long featureId;

	    @Column(columnDefinition = "TEXT", nullable = false)
	    private String description;

	    @ManyToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "product_detail_id")
	    @JsonBackReference
	    private ProductDetails productDetails;

}
