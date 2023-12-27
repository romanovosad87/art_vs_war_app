package com.example.artvswar.dto.request.image;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ImageModerationStatusRequestDto {
    @NotBlank(message = "publicId field is required")
    private String publicId;

    @NotBlank(message = "status field is required (options: APPROVED, REJECTED")
    private String status;
}
