package com.example.artvswar.util;

import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.example.artvswar.dto.response.RefreshTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCreator {
    public ResponseEntity<RefreshTokenResponse> createResponse(AuthenticationResultType resultType) {
        RefreshTokenResponse response = new RefreshTokenResponse();
        response.setIdToken(resultType.getIdToken());
        response.setAccessToken(resultType.getAccessToken());
        response.setExpiresIn(resultType.getExpiresIn());
        response.setTokenType(resultType.getTokenType());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
