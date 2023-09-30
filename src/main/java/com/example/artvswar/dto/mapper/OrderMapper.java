package com.example.artvswar.dto.mapper;

import com.example.artvswar.model.Order;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class OrderMapper {
    public Order toModel(Session session, LocalDateTime timeOfEvent) {
        Order order = new Order();
        order.setCheckoutSessionId(session.getId());
        order.setTotalAmount(BigDecimal.valueOf(session.getAmountTotal()).divide(BigDecimal.valueOf(100), new MathContext(2)));
        order.setSubtotalAmount(BigDecimal.valueOf(session.getAmountSubtotal()).divide(BigDecimal.valueOf(100), new MathContext(2)));
        order.setShippingAmount(BigDecimal.valueOf(session.getShippingCost().getAmountTotal()).divide(BigDecimal.valueOf(100), new MathContext(2)));
        order.setPaymentIntentId(session.getPaymentIntent());
        order.setCreatedAt(timeOfEvent);
        return order;
    }
}
