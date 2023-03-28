package com.example.artvswar.dto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class AuthorRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    private String shortStory;
}
