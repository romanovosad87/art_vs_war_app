package com.example.artvswar.dto.request;

import lombok.Data;

@Data
public class PaintingRequestDto {
    private String paintingTitle;
    private Long authorId;
    private String imageFileName;
}
