package com.example.artvswar.dto.response.painting;

import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PaintingParametersForSearchResponseDto {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Double minWidth;
    private Double maxWidth;
    private Double minHeight;
    private Double maxHeight;
    List<String> styles = new ArrayList<>();
    List<String> mediums = new ArrayList<>();
    List<String> supports = new ArrayList<>();
    List<String> subjects = new ArrayList<>();

    public PaintingParametersForSearchResponseDto(BigDecimal minPrice, BigDecimal maxPrice,
                                                  Double minWidth, Double maxWidth,
                                                  Double minHeight, Double maxHeight) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }
}
