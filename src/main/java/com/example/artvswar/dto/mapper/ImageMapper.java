package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.ImageRequestDto;
import com.example.artvswar.dto.response.image.ImageResponse;
import com.example.artvswar.dto.response.image.ImageResponseDto;
import com.example.artvswar.dto.response.image.RoomViewResponseDto;
import com.example.artvswar.model.Image;
import com.example.artvswar.util.RatioHelper;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.ImageTransformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ImageMapper {
    private final RatioHelper ratioHelper;
    private final CloudinaryClient cloudinaryClient;
    private final ImageTransformation imageTransformation;
    private final RoomViewMapper roomViewMapper;

    public ImageResponseDto toImageResponseDto(Image image) {
        ImageResponseDto dto = new ImageResponseDto();
        dto.setPublicId(image.getId());
        String url = imageTransformation.generateUrl(image.getId(), image.getTransformedRatio());
        dto.setImageUrl(url);
        dto.setRatio(image.getTransformedRatio());
        Set<RoomViewResponseDto> roomViewResponseDtos = image.getRoomViews().stream()
                .map(roomViewMapper::toRoomViewResponseDto)
                .collect(Collectors.toSet());
        dto.setRoomViews(roomViewResponseDtos);
        return dto;
    }

    public ImageResponse toImageResponse(Image image) {
        String url = imageTransformation.generateUrl(image.getId(), image.getTransformedRatio());
        return new ImageResponse(image.getId(), image.getTransformedRatio(), url);
    }

    public Image toImageModel(ImageRequestDto dto) {
        String publicId = dto.getPublicId();
        String version = dto.getVersion();
        String signature = dto.getSignature();

        boolean imageIsValidSignature = cloudinaryClient.verifySignature(publicId, version, signature);
        if (imageIsValidSignature) {
            Image image = new Image();
            image.setId(dto.getPublicId());
            image.setHeight(dto.getHeight());
            image.setWidth(dto.getWidth());
            image.setInitialRatio(dto.getWidth() / (dto.getHeight()));
            image.setTransformedRatio(ratioHelper.getTransformedRatio(
                    dto.getWidth() / (dto.getHeight())));
            return image;
        } else {
            throw new RuntimeException(String.format("The combination of signature: %s and version: "
                    + "%s are not valid for image public_id = %s", signature, version, publicId));
        }
    }
}
