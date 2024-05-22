package com.example.artvswar.dto.response.account;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class AccountResponseDto {
    private Long id;
    private String cognitoSubject;
    private String cognitoUsername;
    private String firstName;
    private String lastName;
    @SerializedName(value = "email")
    private String accountEmailDataEmail;
    private String phone;
}
