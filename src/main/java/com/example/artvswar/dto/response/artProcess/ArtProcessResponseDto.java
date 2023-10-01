package com.example.artvswar.dto.response.artProcess;

import com.example.artvswar.model.enumModel.ModerationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArtProcessResponseDto {
    private Long id;
    private String description;
    private String imagePublicId;
    private String imageUrl;
    private ModerationStatus imageModerationStatus;
}
