package com.example.artvswar.dto.response.painingimage;

public interface PaintingImageViewDto {
    Long getId();
    Double getTransformedRatio();
    ImageView getImage();
    interface ImageView {
        String getUrl();
    }
}
