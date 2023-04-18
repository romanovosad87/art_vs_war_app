package com.example.artvswar.service;

import com.example.artvswar.model.Painting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Map;

public interface PaintingService {
    Painting save(Painting painting);

    Painting get(Long id);

    Page<Painting> getAll(PageRequest pageRequest);

    long getNumberOfAllPaintings();

    Page<Painting> getAllByParams(Map<String, String> params);

    Page<Painting> getAllPaintingsByAuthorId(String id, PageRequest pageRequest);
}
