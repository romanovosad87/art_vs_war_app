package com.example.artvswar.dto.response.author;

import lombok.Data;

@Data
public class AuthorResponseDto {
    private String id;
    private String fullName;
    private String country;
    private String city;
    private String aboutMe;
    private String photoUrl;
}
