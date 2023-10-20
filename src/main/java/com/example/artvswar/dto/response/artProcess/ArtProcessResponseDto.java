package com.example.artvswar.dto.response.artProcess;

import com.example.artvswar.model.enumModel.ModerationStatus;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArtProcessResponseDto {
    private Long id;
    private String description;
    @SerializedName(value = "imagePublicId")
    private String artProcessImageImagePublicId;
    @SerializedName(value = "imageUrl")
    private String artProcessImageImageUrl;
    @SerializedName(value = "imageModerationStatus")
    private ModerationStatus artProcessImageImageModerationStatus;
    @SerializedName(value = "imageWidth")
    private Double artProcessImageWidth;

    @SerializedName(value = "imageHeight")
    private Double artProcessImageHeight;

}
