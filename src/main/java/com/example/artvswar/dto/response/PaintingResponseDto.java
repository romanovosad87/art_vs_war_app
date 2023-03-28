package com.example.artvswar.dto.response;

import lombok.Data;

@Data
public class PaintingResponseDto {
    private Long id;
    private String paintingTitle;
    private AuthorResponseDto author;
    private String imageUrl;
}
