package com.example.artvswar.controller;

import com.example.artvswar.dto.request.email.ContactUsRequestDto;
import com.example.artvswar.dto.request.email.RejectModerationEmailRequestDto;
import com.example.artvswar.service.EmailInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailInternalService internalService;

    @PostMapping("/contactUs")
    public void sendContactUsEmail(@Valid @RequestBody ContactUsRequestDto dto) {
        internalService.contactUsEmail(dto.getEmail(), dto.getMessage());
    }

    @PostMapping("/moderation")
    public void sendRejectionEmail(@Valid @RequestBody RejectModerationEmailRequestDto dto) {
        internalService.sendImageRejectionMail(dto.getPublicId(), dto.getMessage());
    }
}
