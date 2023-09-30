package com.example.artvswar.repository.order;

import com.example.artvswar.dto.response.order.OrderResponseDto;

public interface CustomOrderRepository {
    OrderResponseDto getOrder(Long id);
}
