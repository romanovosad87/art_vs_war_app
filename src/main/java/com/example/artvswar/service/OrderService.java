package com.example.artvswar.service;

import com.example.artvswar.dto.response.order.OrderResponseDto;
import com.example.artvswar.dto.response.order.OrderShortResponseDto;
import com.example.artvswar.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Page<OrderShortResponseDto> getAllOrdersByAccount(String cognitoSubject, Pageable pageable);

    OrderResponseDto getOrder(Long id);
    Order save(Order order);

    void setOrderDelivered(Long id);

    void setOrderDeliveredForMoreThanFiveDays();

    List<Order> getOrdersDeliveredAtBeforeAndDoNotHasTransfer(LocalDateTime time);
}
