package com.example.artvswar.controller;

import com.example.artvswar.dto.request.accountemaildata.AccountEmailDataRequestDto;
import com.example.artvswar.service.AccountEmailDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emailData")
public class AccountEmailDataController {
    private final AccountEmailDataService accountEmailDataService;

    @PostMapping
    public ResponseEntity<Void> changeAccountEmailData(@Valid @RequestBody AccountEmailDataRequestDto dto) {
        accountEmailDataService.updateEmailData(dto);
        return ResponseEntity.noContent().build();
    }
}
