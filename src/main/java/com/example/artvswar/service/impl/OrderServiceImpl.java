package com.example.artvswar.service.impl;

import com.example.artvswar.dto.response.order.OrderResponseDto;
import com.example.artvswar.dto.response.order.OrderShortResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.Order;
import com.example.artvswar.repository.order.OrderRepository;
import com.example.artvswar.service.AccountService;
import com.example.artvswar.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    public static final long DAYS_BEFORE = 5L;
    private final OrderRepository orderRepository;
    private final AccountService accountService;

    @Override
    public Page<OrderShortResponseDto> getAllOrdersByAccount(String cognitoSubject, Pageable pageable) {
        Account account = accountService.getAccountByCognitoSubject(cognitoSubject);
        Page<OrderShortResponseDto> orders = orderRepository
                .findAllByAccount_CognitoSubject(cognitoSubject, pageable);
        List<OrderShortResponseDto> ordersList = orders.getContent().stream()
                .peek(order -> order.setOrderCreatedAt(adjustOffset(order.getCreatedAt(),
                        account.getOffset())))
                .collect(Collectors.toList());

        return new PageImpl<>(ordersList, orders.getPageable(), orders.getTotalElements());
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

    @Override
    @Transactional
    public void setOrderDelivered(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppEntityNotFoundException(
                String.format("Can't find order by id: %s", id)));
        order.setDelivered(true);
        order.setDeliveredAt(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 8,17 * * ?", zone = "ECT")
    public void setOrderDeliveredForMoreThanFiveDays() {
        LocalDateTime fiveDaysBefore = LocalDateTime.now()
                .minusDays(DAYS_BEFORE)
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();
        List<Order> orders = orderRepository
                .getOrdersCreatedBeforeAndAreNotMarkedAsDelivered(fiveDaysBefore);
        orders.forEach(order -> {
            order.setDelivered(true);
            order.setDeliveredAt(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());
        });
    }

    @Override
    @Transactional
    public List<Order> getOrdersDeliveredAtBeforeAndDoNotHasTransfer(LocalDateTime time) {
        return orderRepository.getOrdersDeliveredAtBeforeAndDoNotHasTransfer(time);
    }


    private OffsetDateTime adjustOffset(LocalDateTime localDateTime, int offset) {
        ZoneOffset zoneOffset = ZoneOffset.ofHours(offset);
        return OffsetDateTime.of(localDateTime, zoneOffset);
    }
}
