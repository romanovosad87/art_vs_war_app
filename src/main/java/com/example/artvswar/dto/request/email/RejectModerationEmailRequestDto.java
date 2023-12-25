package com.example.artvswar.dto.request.email;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class RejectModerationEmailRequestDto {

    @NotBlank(message = "public id field is required")
    private String publicId;

    @NotBlank(message = "message field is required")
    String message;
}
