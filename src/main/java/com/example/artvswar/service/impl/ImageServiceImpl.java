package com.example.artvswar.service.impl;

import com.example.artvswar.dto.response.author.AuthorImageModerationResponseDto;
import com.example.artvswar.dto.response.image.PendingRejectImageResponse;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.enumModel.ModerationStatus;
import com.example.artvswar.repository.ImagesRepository;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.util.ImagePublicIdParser;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.ImageTransformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {
    private final ImagesRepository imagesRepository;
    private final ImageTransformation imageTransformation;
    private final CloudinaryClient cloudinaryClient;
    private final AuthorService authorService;
    private final ImagePublicIdParser publicIdParser;

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

    @Override
    public Optional<Image> getImage(String publicId) {
        return imagesRepository.findByPublicId(publicId);
    }

    @Override
    public Page<PendingRejectImageResponse> getImagesByStatus(Pageable pageable, String status) {
        ModerationStatus moderationStatus = null;
        if (status.toUpperCase().equals(ModerationStatus.PENDING.name())) {
            moderationStatus = ModerationStatus.PENDING;
        } else if (status.toUpperCase().equals(ModerationStatus.REJECTED.name())) {
            moderationStatus = ModerationStatus.REJECTED;
        }

        Page<PendingRejectImageResponse> all = imagesRepository
                .getAllByModerationStatus(PendingRejectImageResponse.class, moderationStatus, pageable);
        List<PendingRejectImageResponse> content = all.getContent();
        content.forEach(dto -> {
            String publicId = dto.getPublicId();
            String cognitoSubject = publicIdParser.getCognitoSubject(publicId);
            if (cognitoSubject != null) {
                AuthorImageModerationResponseDto data = authorService.getAuthorDataForImageModeration(cognitoSubject);
                dto.setAuthor(data);
            }
        });
        return new PageImpl<>(content, all.getPageable(), all.getTotalElements());
    }

    @Override
    @Transactional
    public void changeModerationStatus(String publicId, String adminUsername, String status) {
        Image image = imagesRepository.findByPublicId(publicId).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't find image by public id: '%s'", publicId)));
        ModerationStatus moderationStatus = ModerationStatus.valueOf(status.toUpperCase());
        log.info("Moderation status before: " + image.getModerationStatus());
        log.info("Moderation status after: " + moderationStatus.name());
        image.setModerationStatus(moderationStatus);

        cloudinaryClient.addTag(publicId, adminUsername, status);
    }
}
