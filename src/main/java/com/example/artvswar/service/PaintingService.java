package com.example.artvswar.service;

import com.example.artvswar.model.Painting;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.util.Map;

public interface PaintingService {
    Painting save(Painting painting);

    Painting update(Painting painting);

    Painting get(Long id);

    void delete(Painting painting);

    Page<Painting> getAll(Map<String, String> params);

    Page<Painting> getAllByParams(Map<String, String> params);

    Page<Painting> getAllPaintingsByAuthorId(String id, Map<String, String> params);

    long getNumberOfAllPaintings();

    BigDecimal getMaxPrice();

    BigDecimal getMinPrice();

    Integer getMaxHeight();

    Integer getMaxWidth();
}
