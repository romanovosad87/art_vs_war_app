package com.example.artvswar.dto.response.account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountResponseDto {
    private Long id;
    private String cognitoSubject;
    private String cognitoUsername;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
