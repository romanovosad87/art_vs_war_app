package com.example.artvswar.dto.response.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionInPaintingResponseDto {
    private Long id;
    private String prettyId;
    private String title;
}
