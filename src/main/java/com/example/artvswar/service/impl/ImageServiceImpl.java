package com.example.artvswar.service.impl;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.enumModel.ModerationStatus;
import com.example.artvswar.repository.ImagesRepository;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.util.image.ImageTransformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {
    private final ImagesRepository imagesRepository;
    private final ImageTransformation imageTransformation;
    @Override
    @Transactional
    public void updateModerationStatus(String publicId, String status) {
        Image image = imagesRepository.findByPublicId(publicId).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't find image by id: %s", publicId)));
        ModerationStatus moderationStatus = ModerationStatus.valueOf(status.toUpperCase());
        log.info("Moderation status before: " + image.getModerationStatus());
        log.info("Moderation status after: " + moderationStatus.name());
        if (moderationStatus.equals(ModerationStatus.APPROVED)) {
            image.setModerationStatus(moderationStatus);
            image.setUrl(imageTransformation.paintingImageEagerTransformation(publicId));
        } else if (moderationStatus.equals(ModerationStatus.REJECTED)) {
            image.setModerationStatus(moderationStatus);
        }
    }
}
