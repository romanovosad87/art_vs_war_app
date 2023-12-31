package com.example.artvswar.dto.response.shoppingcart;

import com.example.artvswar.dto.response.painting.PaintingShortResponseDto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class ShoppingCartResponseDto {
    private Set<PaintingShortResponseDto> paintings = new LinkedHashSet<>();
    private BigDecimal total;

}
