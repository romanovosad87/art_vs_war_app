package com.example.artvswar.dto.response.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoomViewResponseDto {
    @JsonProperty(value = "url")
    private String imageUrl;
    private double ratio;
}
