package com.techpixe.website.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Entity;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ProductImage {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(name = "imageData", columnDefinition = "LONGBLOB")
    private byte[] imageData; // Stores image data as a binary blob

    @Column(name = "videoData", columnDefinition = "LONGBLOB")
    private byte[] videoData; // Stores video data as a binary blob

    @Column(columnDefinition = "TEXT")
    private String imageDescription; // Description specifically for the image

    @Column(columnDefinition = "TEXT")
    private String videoDescription; // Description specifically for the video

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_details_id")
    @JsonBackReference
    private ProductDetails productDetails;
}

