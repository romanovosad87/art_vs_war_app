package com.example.artvswar.dto.request.image;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ImageCreateRequestDto {
    @NotNull(message = "public_id field is required")
    private String publicId;
    @NotBlank(message = "moderation status field is required")
    private String moderationStatus;
    @NotBlank(message = "version field is required")
    private String version;
    @NotBlank(message = "signature status field is required")
    private String signature;
}
