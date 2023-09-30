package com.example.artvswar.dto.response.author;

import com.example.artvswar.dto.response.painting.PaintingForAllAuthorsDto;
import lombok.Data;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class AuthorsAllStylesDto {
    private String cognitoSubject;
    private transient Map<Long, PaintingForAllAuthorsDto> longPaintingsMap = new HashMap<>();
    private Set<String> styles = new HashSet<>();

    public AuthorsAllStylesDto(String cognitoSubject) {
        this.cognitoSubject = cognitoSubject;
    }
}
