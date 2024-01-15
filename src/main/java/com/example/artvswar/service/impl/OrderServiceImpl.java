package com.example.artvswar.service.impl;

import com.example.artvswar.dto.response.order.OrderDeliveredAtDto;
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
import java.time.Period;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    public static final long DAYS_TO_AUTOMATIC_CONFIRM = 7;
    public static final int ESTIMATED_DELIVERY = 5;
    private final OrderRepository orderRepository;
    private final AccountService accountService;

    @Override
    public Page<OrderShortResponseDto> getAllShortOrdersByAccount(String cognitoSubject,
                                                                  Pageable pageable) {
        Account account = accountService.getAccountByCognitoSubject(cognitoSubject);
        Page<OrderShortResponseDto> orders = orderRepository
                .findAllByAccount_CognitoSubject(cognitoSubject, pageable);
        List<OrderShortResponseDto> content = orders.getContent();
        content.forEach(order -> order.setOrderCreatedAt(adjustOffset(order.getCreatedAt(),
                account.getOffset())));

        return new PageImpl<>(content, orders.getPageable(), orders.getTotalElements());
    }

    @Override
    public OrderResponseDto getOrder(Long id) {
        return orderRepository.getOrder(id);
    }

    @Override
    public Page<OrderResponseDto> getAllFullOrdersByAccount(String cognitoSubject,
                                                            Pageable pageable) {
        Account account = accountService.getAccountByCognitoSubject(cognitoSubject);
        Page<OrderResponseDto> orders = orderRepository.getAllOrdersByAccount(cognitoSubject, pageable);
        List<OrderResponseDto> content = orders.getContent();
        content.forEach(orderDto -> {
            LocalDateTime createdAt = orderDto.getCreatedAt();
            orderDto.setOrderCreatedAt(adjustOffset(createdAt,
                    account.getOffset()));
            if (orderDto.isDelivered()) {
                orderDto.setOrderDeliveredAt(adjustOffset(orderDto.getDeliveredAt(),
                        account.getOffset()));
            } else {
                LocalDateTime estimatedDeliveryAt = createdAt.plusDays(ESTIMATED_DELIVERY);
                orderDto.setOrderEstimatedDeliveryAt(adjustOffset(estimatedDeliveryAt,
                        account.getOffset()));

                LocalDateTime now = LocalDateTime.now();
                Period difference = Period.between(createdAt.toLocalDate(), now.toLocalDate());
                int passedDays = difference.getDays();
                if (passedDays < DAYS_TO_AUTOMATIC_CONFIRM) {
                    int remainingDays = (int) DAYS_TO_AUTOMATIC_CONFIRM - passedDays;
                    String message = remainingDays == 1 ? "1 day" : remainingDays + " days";
                    orderDto.setDaysForAutomaticConfirm(message);
                }
            }
        });

        return new PageImpl<>(content, orders.getPageable(), orders.getTotalElements());

    }

    @Override
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderDeliveredAtDto setOrderDelivered(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppEntityNotFoundException(
                String.format("Can't find order by id: %s", id)));
        order.setDelivered(true);
        order.setDeliveredAt(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());
        LocalDateTime deliveredAt = order.getDeliveredAt();

        Account account = order.getAccount();

        OffsetDateTime deliveredOffsetAt = adjustOffset(deliveredAt, account.getOffset());
        return new OrderDeliveredAtDto(deliveredOffsetAt);
    }

    /**
     * Scheduled method to automatically mark orders as delivered if they
     * were created more than 7 (seven) days ago and are not already marked as delivered.
     * The method is scheduled to run at 8:00 AM and 5:00 PM every day in the
     * Europe/Paris time zone.
     */
    @Override
    @Transactional
    @Scheduled(cron = "0 0 8,17 * * ?", zone = "ECT")
    public void setOrderDeliveredForMoreThanFiveDays() {
        LocalDateTime fiveDaysBefore = LocalDateTime.now()
                .minusDays(DAYS_TO_AUTOMATIC_CONFIRM)
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
