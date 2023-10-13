package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.image.ImageCreateRequestDto;
import com.example.artvswar.dto.response.image.AdditionalImageResponseDto;
import com.example.artvswar.exception.CloudinaryCredentialException;
import com.example.artvswar.model.AdditionalImage;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.enumModel.ModerationStatus;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.ImageTransformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AdditionalImageMapper {

    private final PaintingService paintingService;
    private final ImageTransformation imageTransformation;
    private final CloudinaryClient cloudinaryClient;

    public AdditionalImageResponseDto toDto(AdditionalImage additionalImage) {
        Long id = additionalImage.getId();
        String imageId = additionalImage.getImage().getPublicId();
        String imageUrl = additionalImage.getImage().getUrl();
        ModerationStatus moderationStatus = additionalImage.getImage().getModerationStatus();
        return new AdditionalImageResponseDto(id, imageId, imageUrl, moderationStatus);
    }

    public AdditionalImage toModel(ImageCreateRequestDto imageDto, Long id) {
        AdditionalImage additionalImage = new AdditionalImage();
        additionalImage.setPainting(paintingService.getReference(id));

        boolean validSignature = cloudinaryClient.verifySignature(imageDto.getPublicId(),
                imageDto.getVersion(), imageDto.getSignature());

        if (validSignature) {
            Image image = new Image();
            image.setPublicId(imageDto.getPublicId());
            String url = imageTransformation
                    .paintingImageEagerTransformation(imageDto.getPublicId());
            image.setUrl(url);
            image.setModerationStatus(ModerationStatus.valueOf(imageDto.getModerationStatus()));
            additionalImage.setImage(image);
            return additionalImage;
        } else {
            throw new CloudinaryCredentialException(String.format("The combination of signature: %s and version: "
                            + "%s are not valid for dtoImage public_id = %s",
                    imageDto.getSignature(), imageDto.getVersion(), imageDto.getPublicId()));
        }
    }
}
