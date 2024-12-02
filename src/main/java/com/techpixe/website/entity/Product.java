package com.techpixe.website.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;


    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(nullable = false, length = 255)
    private String productName;

    @Column(columnDefinition = "TEXT")
    private String productDescription;

    private double price;
    private double discountPrice;
    private double offerPrice;
    private Long rating;
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "subCategoryId")
    @JsonBackReference // Ensure unique reference name for SubCategory
    private SubCategory subcategory;

    @OneToMany(mappedBy = "product" ,orphanRemoval = true)
    @JsonManagedReference
    private List<Role> roles = new ArrayList<>();
    
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    @JsonManagedReference
    private ProductDetails productDetails;
    
    
    
}
