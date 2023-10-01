package com.example.artvswar.controller;

import com.example.artvswar.util.AwsCognitoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IndexController {
    private final AwsCognitoClient awsCognitoClient;

    @GetMapping("/")
    public String hello() {
        return "Hello mates!";
    }

    @GetMapping("/delete")
    public String delete(@AuthenticationPrincipal Jwt jwt) {
        awsCognitoClient.deleteUser(jwt);
        return "ok";
    }

    @GetMapping("/signOut")
    public String signOut(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("username");
        System.out.println(username);
        awsCognitoClient.signOutUser(username);
        return "ok";
    }
}
