package com.example.artvswar.service;

import com.example.artvswar.model.Painting;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.Map;

public interface PaintingService {
    Painting save(Painting painting);

    Painting get(Long id);

    List<Painting> getAll(PageRequest pageRequest);

    long getNumberOfAllPaintings();

    List<Painting> getAllByParams(Map<String, String> params);
}
