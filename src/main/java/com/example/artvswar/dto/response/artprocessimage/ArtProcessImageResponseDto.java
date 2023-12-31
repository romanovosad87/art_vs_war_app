package com.example.artvswar.dto.response.artprocessimage;

import com.example.artvswar.model.enummodel.ModerationStatus;
import lombok.Data;

@Data
public class ArtProcessImageResponseDto {
    private String imagePublicId;
    private String imageUrl;
    private Double width;
    private Double height;
    private ModerationStatus imageModerationStatus;
}
