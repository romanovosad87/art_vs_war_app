package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.artProcess.ArtProcessCreateRequestDto;
import com.example.artvswar.dto.request.artProcess.ArtProcessUpdateRequestDto;
import com.example.artvswar.dto.request.image.ImageCreateRequestDto;
import com.example.artvswar.dto.request.image.ImageUpdateRequestDto;
import com.example.artvswar.dto.response.artProcess.ArtProcessResponseDto;
import com.example.artvswar.exception.CloudinaryCredentialException;
import com.example.artvswar.model.ArtProcess;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.enumModel.ModerationStatus;
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
        return new ArtProcessResponseDto(
                artProcess.getId(),
                artProcess.getDescription(),
                artProcess.getImage().getPublicId(),
                artProcess.getImage().getUrl(),
                artProcess.getImage().getModerationStatus());
    }

    public ArtProcess toModel(ArtProcessCreateRequestDto dto) {
        ArtProcess artProcess = new ArtProcess();
        artProcess.setDescription(dto.getDescription().trim());
        ImageCreateRequestDto dtoImage = dto.getImage();
        boolean validSignature = cloudinaryClient.verifySignature(dtoImage.getPublicId(),
                dtoImage.getVersion(), dtoImage.getSignature());
        if (validSignature) {
            Image image = new Image();
            image.setPublicId(dtoImage.getPublicId());
            image.setUrl(imageTransformation.paintingImageEagerTransformation(dtoImage.getPublicId()));
            image.setModerationStatus(ModerationStatus.valueOf(dtoImage.getModerationStatus()));
            artProcess.setImage(image);
        } else {
            throw new CloudinaryCredentialException(String.format("The combination of signature: %s and version: "
                            + "%s are not valid for dtoImage public_id = %s",
                    dtoImage.getSignature(), dtoImage.getVersion(), dtoImage.getPublicId()));
        }
        return artProcess;
    }

    public ArtProcess toModel(ArtProcessUpdateRequestDto dto, ArtProcess artProcessFromDb) {
        artProcessFromDb.setDescription(dto.getDescription().trim());
        ImageUpdateRequestDto dtoImage = dto.getImage();
        if (!dtoImage.getPublicId().equals(artProcessFromDb.getImage().getPublicId())) {
            boolean validSignature = cloudinaryClient.verifySignature(dtoImage.getPublicId(),
                    dtoImage.getVersion(), dtoImage.getSignature());
            if (validSignature) {
                String publicId = artProcessFromDb.getImage().getPublicId();
                Image image = new Image();
                image.setPublicId(dtoImage.getPublicId());
                image.setUrl(imageTransformation.paintingImageEagerTransformation(dtoImage.getPublicId()));
                image.setModerationStatus(ModerationStatus.valueOf(dtoImage.getModerationStatus()));
                artProcessFromDb.setImage(image);
                cloudinaryClient.delete(publicId);
            }
        } else {
            throw new CloudinaryCredentialException(String.format("The combination of signature: %s and version: "
                            + "%s are not valid for dtoImage public_id = %s",
                    dtoImage.getSignature(), dtoImage.getVersion(), dtoImage.getPublicId()));
        }
        return artProcessFromDb;
    }
}
