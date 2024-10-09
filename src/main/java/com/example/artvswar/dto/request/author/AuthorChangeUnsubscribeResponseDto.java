package com.example.artvswar.dto.request.author;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class AuthorChangeUnsubscribeResponseDto {
    @NotNull(message = "unsubscribe field should be present")
    private boolean unsubscribe;
}
