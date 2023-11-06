package com.example.artvswar.dto.response.stripe;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripeBalanceEarningsResponseDto {
    private Double balance;
    private Double totalEarnings;
}
