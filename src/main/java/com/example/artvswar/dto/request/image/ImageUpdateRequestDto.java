package com.example.artvswar.dto.request.image;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ImageUpdateRequestDto {
    @NotNull(message = "public_id field is required")
    private String publicId;
    private String moderationStatus;
    private String version;
    private String signature;
}
