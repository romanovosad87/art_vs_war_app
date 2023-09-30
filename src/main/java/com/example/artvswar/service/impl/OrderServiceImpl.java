package com.example.artvswar.service.impl;

import com.example.artvswar.dto.response.order.OrderResponseDto;
import com.example.artvswar.dto.response.order.OrderShortResponseDto;
import com.example.artvswar.model.Order;
import com.example.artvswar.repository.order.OrderRepository;
import com.example.artvswar.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Page<OrderShortResponseDto> getAllOrdersByAccount(String cognitoSubject, Pageable pageable) {
        return orderRepository.findAllByAccount_CognitoSubject(cognitoSubject, pageable);
    }

    @Override
    public OrderResponseDto getOrder(Long id) {
        return orderRepository.getOrder(id);
    }

    @Override
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
