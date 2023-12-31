package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.image.FullImageCreateRequestDto;
import com.example.artvswar.dto.request.image.FullImageUpdateRequestDto;
import com.example.artvswar.dto.response.image.PaintingImageResponseDto;
import com.example.artvswar.exception.CloudinaryCredentialException;
import com.example.artvswar.model.AdditionalImage;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.PaintingImage;
import com.example.artvswar.model.enummodel.ModerationStatus;
import com.example.artvswar.util.ModerationMockImage;
import com.example.artvswar.util.RatioHelper;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.ImageTransformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PaintingImageMapper {
    private final RatioHelper ratioHelper;
    private final CloudinaryClient cloudinaryClient;
    private final ImageTransformation imageTransformation;

    public PaintingImageResponseDto toImageResponseDto(PaintingImage paintingImage,
                                                       List<AdditionalImage> additionalImages) {
        PaintingImageResponseDto dto = new PaintingImageResponseDto();
        dto.setImagePublicId(paintingImage.getImage().getPublicId());
        ModerationStatus moderationStatus = paintingImage.getImage().getModerationStatus();
        dto.setImageModerationStatus(moderationStatus);
        Set<String> views = dto.getViews();
        if (moderationStatus == ModerationStatus.APPROVED) {
            dto.setImageUrl(paintingImage.getImage().getUrl());
        } else if (moderationStatus == ModerationStatus.PENDING) {
            dto.setImageUrl(ModerationMockImage.PENDING_URL);
        } else if (moderationStatus == ModerationStatus.REJECTED) {
            dto.setImageUrl(ModerationMockImage.REJECTED_URL);
        }

        views.add(dto.getImageUrl());

        additionalImages.forEach(image -> {
            if (image.getImage().getModerationStatus() == ModerationStatus.APPROVED) {
                views.add(image.getImage().getUrl());
            }
        });

        if (moderationStatus == ModerationStatus.APPROVED) {
            paintingImage.getRoomViews().forEach(roomView -> views.add(roomView.getImageUrl()));
        }

        return dto;
    }

    public PaintingImage toImageModel(FullImageCreateRequestDto dto) {
        String publicId = dto.getPublicId();
        String version = dto.getVersion();
        String signature = dto.getSignature();
        String moderationStatus = dto.getModerationStatus();

        boolean imageIsValidSignature = cloudinaryClient.verifySignature(publicId, version, signature);
        if (imageIsValidSignature) {
            PaintingImage paintingImage = new PaintingImage();
            paintingImage.setHeight(dto.getHeight());
            paintingImage.setWidth(dto.getWidth());
            paintingImage.setInitialRatio(dto.getWidth() / (dto.getHeight()));
            paintingImage.setTransformedRatio(ratioHelper.getTransformedRatio(
                    dto.getWidth() / (dto.getHeight())));
            Image image = new Image();
            image.setPublicId(publicId);
            image.setUrl(imageTransformation.paintingImageEagerTransformation(publicId));
            image.setModerationStatus(ModerationStatus.valueOf(moderationStatus));
            paintingImage.setImage(image);
            return paintingImage;
        } else {
            throw new CloudinaryCredentialException(String.format("The combination of signature: %s and version: "
                    + "%s are not valid for image public_id = %s", signature, version, publicId));
        }
    }

    public PaintingImage toImageModel(FullImageUpdateRequestDto dto, PaintingImage paintingImage) {
        String publicId = dto.getPublicId();
        String version = dto.getVersion();
        String signature = dto.getSignature();
        String moderationStatus = dto.getModerationStatus();

        boolean imageIsValidSignature = cloudinaryClient.verifySignature(publicId, version, signature);
        if (imageIsValidSignature) {
            paintingImage.setHeight(dto.getHeight());
            paintingImage.setWidth(dto.getWidth());
            paintingImage.setInitialRatio(dto.getWidth() / (dto.getHeight()));
            paintingImage.setTransformedRatio(ratioHelper.getTransformedRatio(
                    dto.getWidth() / (dto.getHeight())));
            Image image = paintingImage.getImage();
            image.setPublicId(publicId);
            image.setUrl(imageTransformation.paintingImageEagerTransformation(publicId));
            image.setModerationStatus(ModerationStatus.valueOf(moderationStatus));
            paintingImage.setImage(image);
            return paintingImage;
        } else {
            throw new CloudinaryCredentialException(String.format("The combination of signature: %s and version: "
                    + "%s are not valid for image public_id = %s", signature, version, publicId));
        }
    }

    public PaintingImage toImageModelSameImage(FullImageUpdateRequestDto dto,
                                               PaintingImage paintingImage) {

        paintingImage.setHeight(dto.getHeight());
        paintingImage.setWidth(dto.getWidth());
        paintingImage.setInitialRatio(dto.getWidth() / (dto.getHeight()));
        paintingImage.setTransformedRatio(ratioHelper.getTransformedRatio(
                dto.getWidth() / (dto.getHeight())));

        return paintingImage;
    }
}
