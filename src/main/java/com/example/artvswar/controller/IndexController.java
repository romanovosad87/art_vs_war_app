package com.example.artvswar.controller;

import com.example.artvswar.util.AwsCognitoClient;
import com.example.artvswar.util.AwsSESClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IndexController {
    private final AwsCognitoClient awsCognitoClient;
    private final AwsSESClient awsSESClient;

    @GetMapping("/")
    public String hello() {
        return "Hello mates!";
    }

    @GetMapping("/delete")
    public String delete(@AuthenticationPrincipal Jwt jwt) {
        awsCognitoClient.deleteUser(jwt);
        return "ok";
    }


    @GetMapping("/ses/{email}")
    public void sesClient(@PathVariable String email) {
        awsSESClient.createEmailIdentity(email);
    }


    @GetMapping("/ses/status/{email}")
    public String verificationStatus(@PathVariable String email) {
        return awsSESClient.getIdentityVerification(email);
    }
}
