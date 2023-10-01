package com.example.artvswar.dto.response.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShippingAddressResponseDto {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String authorCountry;
    private String countryCode;
    private String postalCode;
    private String phone;
}
