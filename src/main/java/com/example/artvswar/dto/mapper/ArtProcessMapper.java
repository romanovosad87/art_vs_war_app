package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.artprocess.ArtProcessCreateRequestDto;
import com.example.artvswar.dto.request.artprocess.ArtProcessUpdateRequestDto;
import com.example.artvswar.dto.request.image.FullImageCreateRequestDto;
import com.example.artvswar.dto.request.image.FullImageUpdateRequestDto;
import com.example.artvswar.dto.response.artprocess.ArtProcessResponseDto;
import com.example.artvswar.exception.CloudinaryCredentialException;
import com.example.artvswar.model.ArtProcess;
import com.example.artvswar.model.ArtProcessImage;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.enummodel.ModerationStatus;
import com.example.artvswar.util.ModerationMockImage;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.ImageTransformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ArtProcessMapper {
    private final CloudinaryClient cloudinaryClient;
    private final ImageTransformation imageTransformation;

    public ArtProcessResponseDto toDto(ArtProcess artProcess) {
        Long id = artProcess.getId();
        String description = artProcess.getDescription();
        String publicId = artProcess.getArtProcessImage().getImage().getPublicId();
        ModerationStatus moderationStatus = artProcess.getArtProcessImage().getImage().getModerationStatus();
        String url;
        Double width;
        Double height;
        if (moderationStatus.equals(ModerationStatus.APPROVED)) {
            width = artProcess.getArtProcessImage().getWidth();
            height = artProcess.getArtProcessImage().getHeight();
            url = artProcess.getArtProcessImage().getImage().getUrl();
        } else {
            width = ModerationMockImage.IMAGE_WIDTH;
            height = ModerationMockImage.IMAGE_HEIGHT;
            url = ModerationMockImage.PENDING_URL;
        }
        return new ArtProcessResponseDto(id, description, publicId, url, moderationStatus, width, height);
    }

    public ArtProcess toModel(ArtProcessCreateRequestDto dto) {
        ArtProcess artProcess = new ArtProcess();
        artProcess.setDescription(dto.getDescription().trim());
        FullImageCreateRequestDto dtoImage = dto.getImage();
        boolean validSignature = cloudinaryClient.verifySignature(dtoImage.getPublicId(),
                dtoImage.getVersion(), dtoImage.getSignature());
        if (validSignature) {
            ArtProcessImage artProcessImage = new ArtProcessImage();
            artProcessImage.setArtProcess(artProcess);
            artProcessImage.setWidth(dtoImage.getWidth());
            artProcessImage.setHeight(dtoImage.getHeight());
            Image image = new Image();
            image.setPublicId(dtoImage.getPublicId());
            ModerationStatus moderationStatus = ModerationStatus.valueOf(dtoImage.getModerationStatus());
            image.setModerationStatus(moderationStatus);
            image.setUrl(imageTransformation.paintingImageEagerTransformation(dtoImage.getPublicId()));

            artProcessImage.setImage(image);
            artProcess.setArtProcessImage(artProcessImage);
        } else {
            throw new CloudinaryCredentialException(String.format("The combination of signature: %s and version: "
                            + "%s are not valid for dtoImage public_id = %s",
                    dtoImage.getSignature(), dtoImage.getVersion(), dtoImage.getPublicId()));
        }
        return artProcess;
    }

    public ArtProcess toModel(ArtProcessUpdateRequestDto dto, ArtProcess artProcessFromDb) {
        artProcessFromDb.setDescription(dto.getDescription().trim());
        FullImageUpdateRequestDto dtoImage = dto.getImage();
        if (!dtoImage.getPublicId().equals(artProcessFromDb.getArtProcessImage().getImage().getPublicId())) {
            boolean validSignature = cloudinaryClient.verifySignature(dtoImage.getPublicId(),
                    dtoImage.getVersion(), dtoImage.getSignature());
            if (validSignature) {
                ArtProcessImage artProcessImage = new ArtProcessImage();
                Long id = artProcessFromDb.getArtProcessImage().getId();
                artProcessImage.setId(id);
                artProcessImage.setWidth(dtoImage.getWidth());
                artProcessImage.setHeight(dtoImage.getHeight());
                Image image = new Image();
                image.setPublicId(dtoImage.getPublicId());
                image.setUrl(imageTransformation.paintingImageEagerTransformation(dtoImage.getPublicId()));
                image.setModerationStatus(ModerationStatus.valueOf(dtoImage.getModerationStatus()));
                artProcessImage.setImage(image);
                artProcessFromDb.setArtProcessImage(artProcessImage);
                cloudinaryClient.delete(dtoImage.getPublicId());
            }
        } else {
            throw new CloudinaryCredentialException(String.format("The combination of signature: %s and version: "
                            + "%s are not valid for dtoImage public_id = %s",
                    dtoImage.getSignature(), dtoImage.getVersion(), dtoImage.getPublicId()));
        }
        return artProcessFromDb;
    }
}
