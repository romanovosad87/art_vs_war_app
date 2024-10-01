package com.example.artvswar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"postalCode", "addressLine1","addressLine2"})
@Embeddable
public class AccountShippingAddress {
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String addressLine1;
    private String addressLine2;
    @Column(nullable = false)
    private String city;
    private String state;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String countryCode;
    @Column(nullable = false)
    private String postalCode;
}
