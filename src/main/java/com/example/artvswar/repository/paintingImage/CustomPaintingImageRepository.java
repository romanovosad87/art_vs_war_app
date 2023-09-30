package com.example.artvswar.repository.paintingImage;

import com.example.artvswar.model.PaintingImage;

public interface CustomPaintingImageRepository {
    PaintingImage getByIdWithCustomQuery(Long id);
}
