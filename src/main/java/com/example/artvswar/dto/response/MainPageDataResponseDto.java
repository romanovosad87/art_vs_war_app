package com.example.artvswar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MainPageDataResponseDto {
    private long authorsQuantity;
    private long paintingsQuantity;
    private BigDecimal raisedFunds;
}
