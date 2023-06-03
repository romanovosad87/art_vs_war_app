package com.example.artvswar.dto.response.image;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageResponse {
    private String id;
    private Double ratio;
    private String url;
}
