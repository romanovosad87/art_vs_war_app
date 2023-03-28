package com.example.artvswar.dto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class PaintingRequestDto {
    @NotBlank
    private String paintingTitle;
    @Positive
    private Long authorId;
    @NotBlank
    private String imageFileName;
}
