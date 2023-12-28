package com.example.artvswar.dto.response.author;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorImageModerationResponseDto {
    private String prettyId;
    private String fullName;
    private String email;
}
