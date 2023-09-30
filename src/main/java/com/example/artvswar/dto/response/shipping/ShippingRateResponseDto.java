package com.example.artvswar.dto.response.shipping;

import lombok.Data;

@Data
public class ShippingRateResponseDto {
    private Long paintingId;
    private Long shippingPrice;
    private Long deliveryMinDays;
    private Long deliveryMaxDays;
}
