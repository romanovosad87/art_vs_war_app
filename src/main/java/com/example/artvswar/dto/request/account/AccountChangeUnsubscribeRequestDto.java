package com.example.artvswar.dto.request.account;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class AccountChangeUnsubscribeRequestDto {
    @NotNull(message = "unsubscribe field should be present")
    private boolean unsubscribe;
}
