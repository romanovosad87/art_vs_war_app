package com.example.artvswar.dto.response.author;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorResponseDto {
    private String cognitoUsername;
    private String fullName;
    private String country;
    private String city;
    private String aboutMe;
    @JsonProperty(value = "imageUrl")
    private String imageFileName;
}
