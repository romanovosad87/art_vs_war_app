package com.example.artvswar.dto.response;

import lombok.Data;

@Data
public class PictureResponseDto {
    private Long id;
    private String pictureTitle;
    private AuthorResponseDto authorResponseDto;
    private String pictureUrl;
}
