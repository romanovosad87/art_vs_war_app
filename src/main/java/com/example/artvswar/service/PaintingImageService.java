package com.example.artvswar.service;

import com.example.artvswar.model.PaintingImage;

public interface PaintingImageService {

    PaintingImage get(Long id);

    void delete(Long id);
}
