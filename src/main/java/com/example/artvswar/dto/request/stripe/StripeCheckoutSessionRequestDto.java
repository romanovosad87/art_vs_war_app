package com.example.artvswar.dto.request.stripe;

import com.example.artvswar.dto.request.account.AccountShippingAddressRequestDto;
import com.example.artvswar.dto.request.shipping.ShippingRateRequestDto;
import lombok.Data;
import java.util.List;

@Data
public class StripeCheckoutSessionRequestDto {
    private List<ShippingRateRequestDto> shippingRates;

    private AccountShippingAddressRequestDto shippingAddress;

}
