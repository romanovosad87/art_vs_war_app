package com.example.artvswar.dto.response.image;

import com.example.artvswar.model.enummodel.ModerationStatus;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdditionalImageResponseDto {
    private Long id;
    @SerializedName(value = "imagePublicId")
    private String imageId;
    private String imageUrl;
    private ModerationStatus imageModerationStatus;
}
