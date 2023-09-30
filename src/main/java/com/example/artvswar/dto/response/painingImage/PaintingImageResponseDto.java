package com.example.artvswar.dto.response.painingImage;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigInteger;

@Data
@AllArgsConstructor
public class PaintingImageResponseDto {
    private BigInteger id;
    private Double transformedRatio;
    private String url;
}
