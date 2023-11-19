package com.example.artvswar.repository.order;

import com.example.artvswar.dto.response.order.OrderResponseDto;
import com.example.artvswar.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomOrderRepository {
    OrderResponseDto getOrder(Long id);

    Page<OrderResponseDto> getAllOrdersByAccount(String cognitoSubject, Pageable pageable);

    List<Order> getOrdersCreatedBeforeAndAreNotMarkedAsDelivered(LocalDateTime time);

    List<Order> getOrdersDeliveredAtBeforeAndDoNotHasTransfer(LocalDateTime time);

}
