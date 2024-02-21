package com.example.artvswar.dto.request.accountemaildata;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class AccountEmailDataRequestDto {
    @NotBlank
    private String notificationType;
    @NotBlank
    private String email;
}
