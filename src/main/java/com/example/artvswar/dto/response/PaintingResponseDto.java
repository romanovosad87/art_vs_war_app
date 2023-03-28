package com.example.artvswar.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaintingResponseDto {
    private Long id;
    private String title;
    private BigDecimal price;
    private AuthorResponseDto author;
    private String description;
    private StyleResponseDto style;
    private MediumResponseDto medium;
    private int height;
    private int width;
    private String imageUrl;
}
