package com.example.artvswar.dto.request.order;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class OrderRequestDto {
    private BigDecimal subtotalAmount;
    private BigDecimal shippingAmount;
    private Set<Long> paintingIds;
    private String checkoutSessionId;
    private String paymentIntentId;
    private String transferGroup;
}
