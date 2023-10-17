package com.example.artvswar.dto.mapper;

import com.example.artvswar.model.Order;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Log4j2
public class OrderMapper {
    public Order toModel(Session session, LocalDateTime timeOfEvent) {
        Order order = new Order();
        order.setCheckoutSessionId(session.getId());
        log.info("Total amount: " + session.getAmountTotal());
        order.setTotalAmount(BigDecimal.valueOf((double) (session.getAmountTotal()) / 100));
        log.info("Total amount in BigDecimal: " + order.getTotalAmount().doubleValue());
        order.setSubtotalAmount(BigDecimal.valueOf((double) (session.getAmountSubtotal()) / 100));
        order.setShippingAmount(BigDecimal.valueOf((double) (session.getShippingCost().getAmountTotal()) / 100));
        order.setPaymentIntentId(session.getPaymentIntent());
        order.setCreatedAt(timeOfEvent);
        return order;
    }
}
