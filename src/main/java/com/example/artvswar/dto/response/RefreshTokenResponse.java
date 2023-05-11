package com.example.artvswar.dto.response;

import lombok.Data;

@Data
public class RefreshTokenResponse {
    private String idToken;
    private String accessToken;
    private int expiresIn;
    private String tokenType;

}
