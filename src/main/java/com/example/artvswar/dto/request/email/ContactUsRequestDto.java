package com.example.artvswar.dto.request.email;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ContactUsRequestDto {
    @NotNull(message = "email field is required")
    String email;

    @NotNull(message = "message field is required")
    String message;
}
