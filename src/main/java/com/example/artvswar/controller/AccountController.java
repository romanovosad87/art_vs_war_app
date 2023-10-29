package com.example.artvswar.controller;

import com.example.artvswar.dto.request.account.AccountCreateUpdateRequestDto;
import com.example.artvswar.dto.request.account.AccountShippingRequestDto;
import com.example.artvswar.dto.response.account.AccountResponseDto;
import com.example.artvswar.dto.response.account.AccountShippingResponseDto;
import com.example.artvswar.service.AccountService;
import com.example.artvswar.service.stripe.StripeService;
import com.stripe.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
    private static final String SUBJECT = "sub";
    private static final String USERNAME = "username";
    private final AccountService accountService;
    private final StripeService stripeService;

    @PostMapping
    public ResponseEntity<AccountResponseDto> create(@RequestBody @Valid AccountCreateUpdateRequestDto dto,
                                                     @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        String username = jwt.getClaimAsString(USERNAME);
        String name = dto.getFirstName() + " " + dto.getLastName();
        String email = dto.getEmail();
        Customer customer = stripeService.createCustomer(name, email);
        AccountResponseDto savedAccount = accountService.save(dto, subject, username, customer.getId());
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<AccountResponseDto> update(@RequestBody @Valid AccountCreateUpdateRequestDto dto,
                                                     @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        AccountResponseDto updatedAccount = accountService.update(dto, subject);
        return new ResponseEntity<>(updatedAccount, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<AccountResponseDto> getAccount(@AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        AccountResponseDto dto = accountService.get(subject);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/addresses")
    public ResponseEntity<List<AccountShippingResponseDto>> createShippingAddress(
            @RequestBody @Valid List<AccountShippingRequestDto> dtos,
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        List<AccountShippingResponseDto> responseDtoList = accountService.saveAccountShippingAddresses(dtos, subject);
        AccountShippingResponseDto accountShippingResponseDto = responseDtoList.get(responseDtoList.size() - 1);
        return new ResponseEntity<>(List.of(accountShippingResponseDto), HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AccountShippingResponseDto>> getShippingAddresses(
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        List<AccountShippingResponseDto> responseDtoList = accountService.getAccountShippingAddresses(subject);
        AccountShippingResponseDto accountShippingResponseDto = responseDtoList.get(responseDtoList.size() - 1);
        return new ResponseEntity<>(List.of(accountShippingResponseDto), HttpStatus.OK);
    }
}
