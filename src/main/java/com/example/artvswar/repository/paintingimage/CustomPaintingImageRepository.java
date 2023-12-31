package com.example.artvswar.repository.paintingimage;

import com.example.artvswar.model.PaintingImage;

public interface CustomPaintingImageRepository {
    PaintingImage getByIdWithCustomQuery(Long id);
}
