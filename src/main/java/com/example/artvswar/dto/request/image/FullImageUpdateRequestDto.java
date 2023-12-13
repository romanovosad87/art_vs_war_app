package com.example.artvswar.dto.request.image;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class FullImageUpdateRequestDto {
    @NotNull(message = "public_id field is required")
    private String publicId;
    private String moderationStatus;
    private Double width;
    private Double height;
    private String version;
    private String signature;
    private String secureUrl;
}
