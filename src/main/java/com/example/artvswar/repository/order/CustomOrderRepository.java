package com.example.artvswar.repository.order;

import com.example.artvswar.dto.response.order.OrderResponseDto;
import com.example.artvswar.model.Order;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomOrderRepository {
    OrderResponseDto getOrder(Long id);

    List<Order> getOrdersCreatedBeforeAndAreNotMarkedAsDelivered(LocalDateTime time);

    List<Order> getOrdersDeliveredAtBeforeAndDoNotHasTransfer(LocalDateTime time);

}
