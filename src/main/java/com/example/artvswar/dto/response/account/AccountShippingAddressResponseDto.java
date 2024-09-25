package com.example.artvswar.dto.response.account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountShippingAddressResponseDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String countryCode;
    private String postalCode;
}
