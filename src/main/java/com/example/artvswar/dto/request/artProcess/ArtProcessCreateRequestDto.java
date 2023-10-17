package com.example.artvswar.dto.request.artProcess;

import com.example.artvswar.dto.request.image.ImageCreateRequestDto;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ArtProcessCreateRequestDto {
    @Size(max = 150,
            message = "description maximum characters is 150")
    @Pattern(regexp = "[\\p{IsLatin}\\w\\s\\p{P}\\p{S}]*", message = "description except only Latin")
    private String description;

    @NotNull(message = "image file is required")
    private ImageCreateRequestDto image;
}
