package com.example.artvswar.service.impl;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.enumModel.ModerationStatus;
import com.example.artvswar.repository.ImagesRepository;
import com.example.artvswar.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {
    private final ImagesRepository imagesRepository;
    @Override
    @Transactional
    public void updateModerationStatus(String publicId, String status) {
        Image image = imagesRepository.findByPublicId(publicId).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't find image by id: %s", publicId)));
        image.setModerationStatus(ModerationStatus.valueOf(status.toUpperCase()));
    }
}
