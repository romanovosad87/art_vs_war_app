package com.example.artvswar.controller;

import com.example.artvswar.dto.request.authorShippingAddress.AuthorShippingAddressRequestDto;
import com.example.artvswar.dto.response.shipping.ShippingAddressResponseDto;
import com.example.artvswar.service.AuthorShippingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("address/author")
@RequiredArgsConstructor
public class AuthorShippingAddressController {
    private static final String SUBJECT = "sub";
    private final AuthorShippingAddressService authorShippingAddressService;

    @PostMapping
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ShippingAddressResponseDto> create(
            @RequestBody @Valid AuthorShippingAddressRequestDto dto,
            @AuthenticationPrincipal Jwt jwt) {
        String cognitoSubject = jwt.getClaimAsString(SUBJECT);
        ShippingAddressResponseDto responseDto = authorShippingAddressService.create(dto, cognitoSubject);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ShippingAddressResponseDto> update(
            @RequestBody @Valid AuthorShippingAddressRequestDto dto,
            @AuthenticationPrincipal Jwt jwt) {
        String cognitoSubject = jwt.getClaimAsString(SUBJECT);
        ShippingAddressResponseDto responseDto = authorShippingAddressService.update(dto, cognitoSubject);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ShippingAddressResponseDto> getById(@AuthenticationPrincipal Jwt jwt) {
        String cognitoSubject = jwt.getClaimAsString(SUBJECT);
        ShippingAddressResponseDto dto = authorShippingAddressService.findByAuthor(cognitoSubject);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('AUTHOR')")
    public void delete(@AuthenticationPrincipal Jwt jwt) {
        String cognitoSubject = jwt.getClaimAsString(SUBJECT);
        authorShippingAddressService.delete(cognitoSubject);
    }
}
