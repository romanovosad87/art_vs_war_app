package com.example.artvswar.dto.request;

import lombok.Data;

@Data
public class AuthorRequestDto {
    private String id;
    private String fullName;
    private String country;
    private String city;
    private String aboutMe;
    private String imageFileName;
}
