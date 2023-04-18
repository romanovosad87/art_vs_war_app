package com.example.artvswar.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class PaintingRequestDto {
    @NotBlank
    private String title;
    @Positive
    private BigDecimal price;
    @NotBlank
    private String description;
    @Positive
    private Long styleId;
    @Positive
    private Long mediumId;
    @Positive
    private Long supportId;
    @Positive
    private int height;
    @Positive
    private int width;
    @Positive
    private int yearOfCreation;
    @NotBlank
    private String imageFileName;
}
