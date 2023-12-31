package com.example.artvswar.dto.request.artprocess;

import com.example.artvswar.dto.request.image.FullImageUpdateRequestDto;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ArtProcessUpdateRequestDto {
    @Size(max = 150,
            message = "description maximum characters is 150")
    private String description;

    @NotNull(message = "image file is required")
    private FullImageUpdateRequestDto image;
}
