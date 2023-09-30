package com.example.artvswar.repository.paintingImage;

import com.example.artvswar.model.PaintingImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintingImageRepository extends JpaRepository<PaintingImage, Long>,
        CustomPaintingImageRepository {
}
