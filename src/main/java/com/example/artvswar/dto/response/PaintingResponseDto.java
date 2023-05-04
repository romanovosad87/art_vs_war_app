package com.example.artvswar.dto.response;

import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.util.DateTimePatternUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaintingResponseDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private int height;
    private int width;
    private int yearOfCreation;
    private AuthorForPaintingResponseDto author;
    private StyleResponseDto style;
    private MediumResponseDto medium;
    private SupportResponseDto support;
    private String imageUrl;
    @JsonFormat(pattern = DateTimePatternUtil.DATE_TIME_PATTERN)
    private LocalDateTime entityCreatedAt;
}
