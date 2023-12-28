package com.example.artvswar.dto.response.order;

import com.example.artvswar.util.gson.JsonLocalDateTimeOrderCreatedAtAdapter;
import com.google.gson.annotations.JsonAdapter;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class OrderShortResponseDto {
    private Long id;

    private BigDecimal totalAmount;
    private transient LocalDateTime createdAt;

    @JsonAdapter(JsonLocalDateTimeOrderCreatedAtAdapter.class)
    private OffsetDateTime orderCreatedAt;

    public OrderShortResponseDto(Long id, BigDecimal totalAmount, LocalDateTime createdAt) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }
}
