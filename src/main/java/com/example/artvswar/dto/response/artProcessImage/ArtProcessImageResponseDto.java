package com.example.artvswar.dto.response.artProcessImage;

import com.example.artvswar.model.enumModel.ModerationStatus;
import lombok.Data;

@Data
public class ArtProcessImageResponseDto {
    private String imagePublicId;
    private String imageUrl;
    private Double width;
    private Double height;
    private ModerationStatus imageModerationStatus;
}
