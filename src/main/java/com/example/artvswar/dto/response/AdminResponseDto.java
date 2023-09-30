package com.example.artvswar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminResponseDto {
    private String cognitoSubject;

    private String cognitoUsername;
    private String fullName;

    private String prettyId;

    private String email;
}
