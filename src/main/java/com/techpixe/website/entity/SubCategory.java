package com.techpixe.website.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryId;

    @Column(nullable = false, unique = true)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference // Prevent recursion by ignoring the back reference to Category
    private Category category;
    
    @OneToMany(mappedBy = "subcategory") // corrected mappedBy
    @JsonManagedReference // To correctly serialize the products
    private List<Product> products;
}
