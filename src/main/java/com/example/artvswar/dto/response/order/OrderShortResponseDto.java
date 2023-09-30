package com.example.artvswar.dto.response.order;

import com.example.artvswar.util.gson.JsonLocalDateTimeOrderAdapter;
import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderShortResponseDto {
    private Long id;
    private BigDecimal totalAmount;
    @JsonAdapter(JsonLocalDateTimeOrderAdapter.class)
    private LocalDateTime createdAt;
}
