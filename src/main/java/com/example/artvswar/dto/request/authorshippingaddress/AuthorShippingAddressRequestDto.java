package com.example.artvswar.dto.request.authorshippingaddress;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class AuthorShippingAddressRequestDto {
    @NotBlank(message = "addressLine1 field is required")
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    @NotBlank(message = "city field is required")
    private String city;
    private String state;
    @NotBlank(message = "country field is required")
    private String country;
    @NotBlank(message = "postcode field is required")
    private String postalCode;
    @Pattern(regexp = "[\\d-()]*", message = "phone field except only digits, '(', ')' and '-'")
    private String phone;
}
