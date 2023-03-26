package com.example.artvswar.service;

import com.example.artvswar.model.Painting;
import java.util.List;

public interface PaintingService {
    Painting save(Painting painting);

    Painting get(Long id);

    List<Painting> getAll();
}
