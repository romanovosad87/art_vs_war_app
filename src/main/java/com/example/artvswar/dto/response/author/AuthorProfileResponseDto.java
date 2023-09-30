package com.example.artvswar.dto.response.author;

import com.example.artvswar.model.enumModel.ModerationStatus;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "cognitoSubject")
public class AuthorProfileResponseDto {
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
    @SerializedName(value = "imageModerationStatus")
    private ModerationStatus authorPhotoImageModerationStatus;
    private Set<String> styles = new HashSet<>();

    public AuthorProfileResponseDto(String cognitoSubject, String prettyId, String fullName,
                                    String country, String city, String aboutMe,
                                    String authorPhotoImagePublicId, String authorPhotoImageUrl,
                                    ModerationStatus authorPhotoImageModerationStatus) {
        this.cognitoSubject = cognitoSubject;
        this.prettyId = prettyId;
        this.fullName = fullName;
        this.country = country;
        this.city = city;
        this.aboutMe = aboutMe;
        this.authorPhotoImagePublicId = authorPhotoImagePublicId;
        this.authorPhotoImageUrl = authorPhotoImageUrl;
        this.authorPhotoImageModerationStatus = authorPhotoImageModerationStatus;
    }
}
