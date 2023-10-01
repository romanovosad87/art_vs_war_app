package com.example.artvswar.dto.request.shipping;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ShippingRateRequestDto {
    @NotNull
    private Long totalShippingPrice;
    @NotNull
    private Long totalDeliveryMinDays;
    @NotNull
    private Long totalDeliveryMaxDays;
}
