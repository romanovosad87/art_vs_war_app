package com.example.artvswar.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaintingResponseDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private int height;
    private int width;
    private AuthorResponseDto author;
    private StyleResponseDto style;
    private MediumResponseDto medium;
    private SupportResponseDto support;
    private String imageUrl;
}
