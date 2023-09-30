package com.example.artvswar.controller;

import com.example.artvswar.dto.request.email.ContactUsRequestDto;
import com.example.artvswar.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/test/{email}")
    public String sendTestEmail(@PathVariable String email) {
        return emailService.sendTestEmail(email);
    }

    @PostMapping("/contactUs")
    public void sendContactUsEmail(@Valid @RequestBody ContactUsRequestDto dto) {
        emailService.contactUsEmail(dto.getEmail(), dto.getMessage());
    }
}
