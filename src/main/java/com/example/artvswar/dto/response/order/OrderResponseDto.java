package com.example.artvswar.dto.response.order;

import com.example.artvswar.dto.response.painting.PaintingShortResponseDto;
import com.example.artvswar.util.gson.JsonLocalDateTimeOrderAdapter;
import com.google.gson.annotations.JsonAdapter;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class OrderResponseDto {
    private Long id;
    private BigDecimal totalAmount;
    private BigDecimal subtotalAmount;
    private BigDecimal shippingAmount;
    private boolean isDelivered;
    @JsonAdapter(JsonLocalDateTimeOrderAdapter.class)
    private OffsetDateTime orderEstimatedDeliveryAt;
    private transient LocalDateTime deliveredAt;
    @JsonAdapter(JsonLocalDateTimeOrderAdapter.class)
    private OffsetDateTime orderDeliveredAt;
    private transient LocalDateTime createdAt;
    @JsonAdapter(JsonLocalDateTimeOrderAdapter.class)
    private OffsetDateTime orderCreatedAt;
    private String daysForAutomaticConfirm;
    private Set<PaintingShortResponseDto> paintings = new LinkedHashSet<>();

    public OrderResponseDto(Long id, BigDecimal totalAmount, BigDecimal subtotalAmount,
                            BigDecimal shippingAmount, boolean isDelivered, LocalDateTime deliveredAt,
                            LocalDateTime createdAt) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.subtotalAmount = subtotalAmount;
        this.shippingAmount = shippingAmount;
        this.isDelivered = isDelivered;
        this.deliveredAt = deliveredAt;
        this.createdAt = createdAt;
    }
}
