package com.example.artvswar.dto.response.collection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CollectionShortResponseDto {
    private Long id;
    private String prettyId;
    private String title;
    private String description;
    private String authorFullName;
    private String authorPrettyId;
}
