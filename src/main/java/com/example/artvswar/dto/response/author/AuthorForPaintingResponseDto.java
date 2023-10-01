package com.example.artvswar.dto.response.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorForPaintingResponseDto {
    private String id;
    private String prettyId;
    private String fullName;
    private String country;
}
