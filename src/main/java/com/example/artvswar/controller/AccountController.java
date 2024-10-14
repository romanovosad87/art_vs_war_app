package com.example.artvswar.controller;

import com.example.artvswar.dto.request.account.AccountChangeUnsubscribeRequestDto;
import com.example.artvswar.dto.request.account.AccountCreateUpdateRequestDto;
import com.example.artvswar.dto.request.account.AccountShippingAddressRequestDto;
import com.example.artvswar.dto.response.account.AccountResponseDto;
import com.example.artvswar.dto.response.account.AccountShippingAddressResponseDto;
import com.example.artvswar.service.AccountService;
import com.example.artvswar.service.stripe.StripeService;
import com.stripe.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;

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
        String name = dto.getFirstName().trim() + " " + dto.getLastName().trim();
        String email = dto.getEmail().trim();
        Customer customer = stripeService.createCustomer(name, email);
        AccountResponseDto savedAccount = accountService.save(dto, subject, username, customer.getId());
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
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

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/addresses")
    public ResponseEntity<List<AccountShippingAddressResponseDto>> createShippingAddress(
            @RequestBody @Valid List<AccountShippingAddressRequestDto> dtos,
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        List<AccountShippingAddressResponseDto> responseDtoList = accountService.saveAccountShippingAddresses(dtos, subject);
        AccountShippingAddressResponseDto accountShippingAddressResponseDto = responseDtoList.get(responseDtoList.size() - 1);
        return new ResponseEntity<>(List.of(accountShippingAddressResponseDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/addresses")
    public ResponseEntity<List<AccountShippingAddressResponseDto>> getShippingAddresses(
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        List<AccountShippingAddressResponseDto> responseDtoList = accountService.getAccountShippingAddresses(subject);
        List<AccountShippingAddressResponseDto> newResponseDtoList = new ArrayList<>();
        if (!responseDtoList.isEmpty()) {
            AccountShippingAddressResponseDto accountShippingAddressResponseDto = responseDtoList.get(responseDtoList.size() - 1);
            newResponseDtoList.add(accountShippingAddressResponseDto);
        }

        return new ResponseEntity<>(newResponseDtoList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PatchMapping("/unsubscribe")
    public ResponseEntity<Void> changeUnsubscribeEmailStatus(@Valid @RequestBody
                                                          AccountChangeUnsubscribeRequestDto dto,
                                                          @AuthenticationPrincipal Jwt jwt) {
        String accountSubject = jwt.getClaimAsString(SUBJECT);
        accountService.changeUnsubscribeEmailStatus(accountSubject, dto.isUnsubscribe());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal Jwt jwt) {
        String cognitoSubject = jwt.getClaimAsString(SUBJECT);
        accountService.delete(cognitoSubject, jwt);
        return ResponseEntity.noContent().build();
    }
}
