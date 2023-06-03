package com.example.artvswar.dto.response.painting;

import com.example.artvswar.model.Author;
import com.example.artvswar.model.Medium;
import com.example.artvswar.model.Style;
import com.example.artvswar.model.Support;
import com.example.artvswar.util.DateTimePatternUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PaintingTestResponseDto {
    private Long id;
    private double height;
    private double width;
    private int yearOfCreation;
    private Author author;
    private List<Style> styles;
    private List<Medium> mediums;
    private List<Support> supports;
    private String imageUrl;
    @JsonFormat(pattern = DateTimePatternUtil.DATE_TIME_PATTERN)
    private LocalDateTime entityCreatedAt;
}
