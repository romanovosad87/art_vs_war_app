package com.example.artvswar.dto.request.shipping;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class AddressRequestDto {

    private String phone;
    @NotNull
    private String addressLine1;


    private String addressLine2;


    private String city;


    private String state;

    private String country;


    private String postalCode;
}
