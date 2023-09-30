package com.example.artvswar.dto.request.account;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AccountShippingRequestDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotEmpty
    private String phone;
    @NotBlank(message = "addressLine1 field is required")
    private String addressLine1;
    private String addressLine2;
    @NotBlank(message = "city field is required")
    private String city;
    @NotBlank(message = "state field is required")
    private String state;
    @NotBlank(message = "country field is required")
    private String country;
    @NotBlank(message = "postcode field is required")
    private String postalCode;
}
