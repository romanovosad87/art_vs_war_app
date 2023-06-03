package com.example.artvswar.service;

import com.example.artvswar.dto.request.painting.PaintingCreateRequestDto;
import com.example.artvswar.dto.response.painting.PaintingDto;
import com.example.artvswar.dto.response.painting.PaintingResponseDto;
import com.example.artvswar.model.Painting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.Map;

public interface PaintingService {
    Painting save(PaintingCreateRequestDto dto, String cognitoUsername);

    Painting update(Painting painting);

    Painting get(Long id);

    PaintingResponseDto getDto(Long id);

    void delete(Painting painting);

    Page<PaintingDto> getAllByParamsReturnDto(Map<String, String> params, Pageable pageable);

    long getNumberOfAllPaintings();

    BigDecimal getMaxPrice();

    BigDecimal getMinPrice();

    Integer getMaxHeight();

    Integer getMaxWidth();
}
