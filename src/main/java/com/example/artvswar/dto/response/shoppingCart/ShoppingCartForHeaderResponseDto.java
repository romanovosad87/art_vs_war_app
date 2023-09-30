package com.example.artvswar.dto.response.shoppingCart;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ShoppingCartForHeaderResponseDto {
    private long totalNumber;
    private BigDecimal totalPrice;
}
