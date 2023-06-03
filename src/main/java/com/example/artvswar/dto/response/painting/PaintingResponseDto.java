package com.example.artvswar.dto.response.painting;

import com.example.artvswar.dto.response.MediumResponseDto;
import com.example.artvswar.dto.response.StyleResponseDto;
import com.example.artvswar.dto.response.SubjectResponseDto;
import com.example.artvswar.dto.response.SupportResponseDto;
import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.dto.response.image.ImageResponseDto;
import com.example.artvswar.util.DateTimePatternUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaintingResponseDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Double height;
    private Double width;
    private Integer yearOfCreation;
    @JsonFormat(pattern = DateTimePatternUtil.DATE_TIME_PATTERN)
    private LocalDateTime addedToDatabase;
    private AuthorForPaintingResponseDto author;
    private List<StyleResponseDto> styles;
    private List<MediumResponseDto> mediums;
    private List<SupportResponseDto> supports;
    private List<SubjectResponseDto> subjects;
    @JsonProperty(value = "image")
    private ImageResponseDto imageResponseDto;
}
