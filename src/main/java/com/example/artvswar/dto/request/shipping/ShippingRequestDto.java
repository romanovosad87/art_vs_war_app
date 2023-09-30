package com.example.artvswar.dto.request.shipping;

import lombok.Data;
import lombok.ToString;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class ShippingRequestDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotEmpty
    private String phone;
    @NotNull
    private String addressLine1;
    @NotNull
    private String addressLine2;
    @NotNull
    private String city;
    @NotNull
    private String state;
    @NotNull
    private String country;
    @NotNull
    private String postalCode;
}
