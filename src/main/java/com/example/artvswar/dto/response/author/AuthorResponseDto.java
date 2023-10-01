package com.example.artvswar.dto.response.author;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "cognitoSubject")
public class AuthorResponseDto {
    private String cognitoSubject;
    private String prettyId;
    private String fullName;
    private String country;
    private String city;
    private String aboutMe;
    @SerializedName(value = "imagePublicId")
    private String authorPhotoImagePublicId;
    @SerializedName(value = "imageUrl")
    private String authorPhotoImageUrl;

    private Set<String> styles = new HashSet<>();

    public AuthorResponseDto(String cognitoSubject, String prettyId, String fullName,
                             String country,
                             String city, String aboutMe, String authorPhotoImagePublicId,
                             String authorPhotoImageUrl) {
        this.cognitoSubject = cognitoSubject;
        this.prettyId = prettyId;
        this.fullName = fullName;
        this.country = country;
        this.city = city;
        this.aboutMe = aboutMe;
        this.authorPhotoImagePublicId = authorPhotoImagePublicId;
        this.authorPhotoImageUrl = authorPhotoImageUrl;
    }
}
