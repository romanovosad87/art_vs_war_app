package com.example.artvswar.util;

import com.example.artvswar.dto.response.FolderResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CloudinaryFolderCreator {
    private static final String REGEX = "/\\w+$";
    private static final String PHOTO_FOLDER = "art-app/images/%s/photo";
    private static final String PAINTING_IMAGE_FOLDER = "art-app/images/%s/paintings/%s";
    private static final String ART_PROCESS_FOLDER = "art-app/images/%s/art-process";

    public FolderResponseDto createForAuthorPhoto(String cognitoSubject) {
        String folder = String.format(PHOTO_FOLDER, cognitoSubject);
        return new FolderResponseDto(folder);
    }

    public FolderResponseDto createForPaintingImage(String cognitoSubject, String prettyId) {
        String folder = String.format(PAINTING_IMAGE_FOLDER, cognitoSubject, prettyId);
        return new FolderResponseDto(folder);
    }

    public FolderResponseDto createForAtrProcess(String cognitoSubject) {
        String folder = String.format(ART_PROCESS_FOLDER, cognitoSubject);
        return new FolderResponseDto(folder);
    }

    public FolderResponseDto createForAdditionalImage(String imagePublicId) {
        String folder = imagePublicId.replaceAll(REGEX, "");
        return new FolderResponseDto(folder);
    }
}
