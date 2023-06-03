package com.example.artvswar.dto.response.image;

import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
public class ImageResponseDto {
    private String publicId;
    private String imageUrl;
    private double ratio;
    private Set<RoomViewResponseDto> roomViews = new HashSet<>();
}
