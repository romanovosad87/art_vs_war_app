package com.example.artvswar.dto.response.painting;

import com.example.artvswar.dto.response.MediumResponseDto;
import com.example.artvswar.dto.response.StyleResponseDto;
import com.example.artvswar.dto.response.SubjectResponseDto;
import com.example.artvswar.dto.response.SupportResponseDto;
import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.dto.response.image.ImageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class PaintingDto {
    private Long id;
    private String prettyId;
    private String title;
    private String description;
    private BigDecimal price;
    private Double height;
    private Double width;
    private Integer yearOfCreation;
    private ImageResponse image;
    private AuthorForPaintingResponseDto author;
    private List<StyleResponseDto> styles = new ArrayList<>();
    private List<MediumResponseDto> mediums = new ArrayList<>();
    private List<SupportResponseDto> supports = new ArrayList<>();
    private List<SubjectResponseDto> subjects = new ArrayList<>();

    public PaintingDto(Long id, String prettyId, String title, String description, BigDecimal price, Double height,
                       Double width, Integer yearOfCreation) {
        this.id = id;
        this.prettyId = prettyId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.height = height;
        this.width = width;
        this.yearOfCreation = yearOfCreation;
    }
}
