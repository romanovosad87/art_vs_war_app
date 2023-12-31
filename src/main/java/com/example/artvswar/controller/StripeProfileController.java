package com.example.artvswar.controller;

import com.example.artvswar.dto.response.stripeprofile.CheckStripeAccountDetailsSubmittedResponseDto;
import com.example.artvswar.service.StripeProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stripeAccount")
@RequiredArgsConstructor
public class StripeProfileController {
    private static final String COGNITO_SUBJECT = "sub";
    private final StripeProfileService stripeProfileService;

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/check")
    public ResponseEntity<CheckStripeAccountDetailsSubmittedResponseDto> getAccount(@AuthenticationPrincipal Jwt jwt) {
        String authorCognitoSubject = jwt.getClaimAsString(COGNITO_SUBJECT);
        var dto = new CheckStripeAccountDetailsSubmittedResponseDto();
        boolean submitted = stripeProfileService.checkIfDetailsSubmitted(authorCognitoSubject);
        dto.setAccountFullyCreated(submitted);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
