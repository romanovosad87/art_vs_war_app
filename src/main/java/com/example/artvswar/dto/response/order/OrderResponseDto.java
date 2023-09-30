package com.example.artvswar.dto.response.order;

import com.example.artvswar.dto.response.painting.PaintingShortResponseDto;
import com.example.artvswar.util.gson.JsonLocalDateTimeOrderAdapter;
import com.google.gson.annotations.JsonAdapter;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class OrderResponseDto {
    private Long id;
    private BigDecimal totalAmount;
    private BigDecimal subtotalAmount;
    private BigDecimal shippingAmount;
    @JsonAdapter(JsonLocalDateTimeOrderAdapter.class)
    private LocalDateTime createdAt;
    private Set<PaintingShortResponseDto> paintings = new LinkedHashSet<>();

    public OrderResponseDto(Long id, BigDecimal totalAmount, BigDecimal subtotalAmount,
                            BigDecimal shippingAmount, LocalDateTime createdAt) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.subtotalAmount = subtotalAmount;
        this.shippingAmount = shippingAmount;
        this.createdAt = createdAt;
    }
}
