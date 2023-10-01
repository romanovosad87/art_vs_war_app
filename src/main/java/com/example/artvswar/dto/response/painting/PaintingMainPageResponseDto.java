package com.example.artvswar.dto.response.painting;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaintingMainPageResponseDto {
    private String prettyId;
    private String authorPrettyId;
    private String url;
}
