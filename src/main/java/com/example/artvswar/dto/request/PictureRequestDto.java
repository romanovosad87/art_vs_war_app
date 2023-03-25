package com.example.artvswar.dto.request;

import lombok.Data;

@Data
public class PictureRequestDto {
    private String pictureTitle;
    private Long authorId;
    private String pictureUrl;
}
