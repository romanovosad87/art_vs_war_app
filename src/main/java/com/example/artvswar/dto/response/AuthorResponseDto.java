package com.example.artvswar.dto.response;

import lombok.Data;

@Data
public class AuthorResponseDto {
    private Long id;
    private String name;
    private String country;
    private String city;
    private String shortStory;
}
