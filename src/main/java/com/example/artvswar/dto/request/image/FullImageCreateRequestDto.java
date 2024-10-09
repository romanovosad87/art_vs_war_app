package com.example.artvswar.dto.request.image;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class FullImageCreateRequestDto {
    @NotNull(message = "public_id field is required")
    private String publicId;

    @NotBlank(message = "moderation status field is required")
    private String moderationStatus;
    @NotNull(message = "width field is required")
    private Double width;
    @NotNull(message = "height field is required")
    private Double height;

    @NotNull(message = "version field is required")
    private String version;

    @NotNull(message = "signature field is required")
    private String signature;

    private String secureUrl;
}
