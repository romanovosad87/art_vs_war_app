package com.example.artvswar.dto.request.admin;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class AdminRequestDto {

    @NotNull(message = "Cognito subject could not be null")
    private String cognitoSubject;
}
