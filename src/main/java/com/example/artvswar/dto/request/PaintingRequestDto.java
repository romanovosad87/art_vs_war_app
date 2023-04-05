package com.example.artvswar.dto.request;

import com.example.artvswar.util.DateTimePatternUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

@Data
public class PaintingRequestDto {
    @NotBlank
    private String title;
    @Positive
    private BigDecimal price;
    @Positive
    private Long authorId;
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
    @JsonFormat(pattern = DateTimePatternUtil.YEAR_PATTERN)
    @PastOrPresent
    private int yearOfCreation;
    @NotBlank
    private String imageFileName;
}
