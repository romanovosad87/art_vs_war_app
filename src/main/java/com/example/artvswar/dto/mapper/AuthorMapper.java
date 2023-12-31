package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.author.AuthorCreateRequestDto;
import com.example.artvswar.dto.request.author.AuthorUpdateRequestDto;
import com.example.artvswar.dto.request.image.ImageCreateRequestDto;
import com.example.artvswar.dto.request.image.ImageUpdateRequestDto;
import com.example.artvswar.dto.response.author.AuthorProfileResponseDto;
import com.example.artvswar.exception.CloudinaryCredentialException;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.AuthorPhoto;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.enummodel.ModerationStatus;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.ImageTransformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthorMapper {

    private final CloudinaryClient cloudinaryClient;
    private final ImageTransformation imageTransformation;

    public Author toAuthorModel(AuthorCreateRequestDto dto) {
        Author author = new Author();
        author.setFullName(dto.getFullName().trim());
        author.setCountry(dto.getCountry().trim());
        author.setCity(dto.getCity().trim());
        author.setAboutMe(dto.getAboutMe().trim());
        author.setEmail(dto.getEmail().trim());
        ImageCreateRequestDto dtoImage = dto.getImage();

        boolean validSignature = cloudinaryClient.verifySignature(dtoImage.getPublicId(),
                dtoImage.getVersion(), dtoImage.getSignature());
        if (validSignature) {
            AuthorPhoto authorPhoto = new AuthorPhoto();
            authorPhoto.setAuthor(author);
            Image image = new Image();
            image.setPublicId(dtoImage.getPublicId());
            image.setUrl(imageTransformation.photoImageEagerTransformation(dtoImage.getPublicId()));
            image.setModerationStatus(ModerationStatus.valueOf(dtoImage.getModerationStatus()));
            authorPhoto.setImage(image);
            author.setAuthorPhoto(authorPhoto);
        } else {
            throw new CloudinaryCredentialException(String.format("The combination of signature: %s and version: "
                            + "%s are not valid for dtoImage public_id = %s",
                    dtoImage.getSignature(), dtoImage.getVersion(), dtoImage.getPublicId()));
        }
        return author;
    }

    public Author updateAuthorModel(AuthorUpdateRequestDto dto, Author authorFromDB) {
        authorFromDB.setFullName(dto.getFullName().trim());
        authorFromDB.setCountry(dto.getCountry().trim());
        authorFromDB.setCity(dto.getCity().trim());
        authorFromDB.setAboutMe(dto.getAboutMe().trim());
        authorFromDB.setDeleted(dto.isDeactivated());
        ImageUpdateRequestDto dtoImage = dto.getImage();

        if (!dtoImage.getPublicId().equals(authorFromDB.getAuthorPhoto().getImage().getPublicId())) {
            boolean validSignature = cloudinaryClient.verifySignature(dtoImage.getPublicId(),
                    dtoImage.getVersion(), dtoImage.getSignature());
            if (validSignature) {
                String publicId = authorFromDB.getAuthorPhoto().getImage().getPublicId();
                Image image = authorFromDB.getAuthorPhoto().getImage();
                image.setPublicId(dtoImage.getPublicId());
                image.setUrl(imageTransformation.photoImageEagerTransformation(dtoImage.getPublicId()));
                image.setModerationStatus(ModerationStatus.valueOf(dtoImage.getModerationStatus()));
                cloudinaryClient.delete(publicId);
            } else {
                throw new CloudinaryCredentialException(String.format("The combination of signature: %s and version: "
                                + "%s are not valid for dtoImage public_id = %s",
                        dtoImage.getSignature(), dtoImage.getVersion(), dtoImage.getPublicId()));
            }
        }
        return authorFromDB;
    }

    public AuthorProfileResponseDto toDto(Author author) {
        String cognitoSubject = author.getCognitoSubject();
        String prettyId = author.getPrettyId();
        String fullName = author.getFullName();
        String country = author.getCountry();
        String city = author.getCity();
        String aboutMe = author.getAboutMe();
        boolean isDeleted = author.isDeleted();
        Image image = author.getAuthorPhoto().getImage();
        String publicId = image.getPublicId();
        String url = image.getUrl();
        ModerationStatus moderationStatus = image.getModerationStatus();
        return new AuthorProfileResponseDto(
                cognitoSubject,
                prettyId,
                fullName,
                country,
                city,
                aboutMe,
                isDeleted,
                publicId,
                url,
                moderationStatus
        );
    }
}
