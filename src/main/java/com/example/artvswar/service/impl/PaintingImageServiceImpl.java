package com.example.artvswar.service.impl;

import com.example.artvswar.model.PaintingImage;
import com.example.artvswar.repository.paintingImage.PaintingImageRepository;
import com.example.artvswar.service.PaintingImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaintingImageServiceImpl implements PaintingImageService {
    private final PaintingImageRepository paintingImageRepository;

    @Override
    public PaintingImage getReference(Long id) {
        return paintingImageRepository.getReferenceById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        paintingImageRepository.deleteById(id);
    }
}
