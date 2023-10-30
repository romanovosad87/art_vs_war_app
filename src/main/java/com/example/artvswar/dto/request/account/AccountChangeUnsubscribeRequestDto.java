package com.example.artvswar.dto.request.account;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class AccountChangeUnsubscribeRequestDto {
    @NotNull(message = "unsubscribe field should be present")
    private boolean unsubscribe;
}
