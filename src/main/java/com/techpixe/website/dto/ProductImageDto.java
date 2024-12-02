package com.techpixe.website.dto;

import lombok.Data;

@Data
public class ProductImageDto {
	
    private Long imageId;

  
    private byte[] imageData; // Stores image data as a binary blob

    private byte[] videoData; // Stores video data as a binary blob

    private String imageDescription; // Description specifically for the image

    private String videoDescription; // Description specifically for the video

}
