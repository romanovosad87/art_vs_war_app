package com.example.artvswar.dto.mapper;

import com.example.artvswar.model.Order;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@org.junit.jupiter.api.Order(360)
@SpringBootTest
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @MockBean
    private Session session;

    private Session.ShippingCost shippingCost;

    private static final String SESSION_ID = "sess_123456";
    private static final long AMOUNT_TOTAL = 1200; // 12.00 in actual currency units
    private static final long AMOUNT_SUBTOTAL = 1000; // 10.00 in actual currency units
    private static final long SHIPPING_AMOUNT = 200; // 2.00 in actual currency units
    private static final String PAYMENT_INTENT_ID = "pi_123456";
    private static final LocalDateTime TIME_OF_EVENT = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        when(session.getId()).thenReturn(SESSION_ID);
        when(session.getAmountTotal()).thenReturn(AMOUNT_TOTAL);
        when(session.getAmountSubtotal()).thenReturn(AMOUNT_SUBTOTAL);

        // Proper mock setup for ShippingCost
        shippingCost = mock(Session.ShippingCost.class);
        when(shippingCost.getAmountTotal()).thenReturn(SHIPPING_AMOUNT);
        when(session.getShippingCost()).thenReturn(shippingCost);

        when(session.getPaymentIntent()).thenReturn(PAYMENT_INTENT_ID);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(session);
    }

    @Test
    @DisplayName("toModel - Successfully creates Order model from Session")
    void testToModel_Success() {
        // Act
        Order order = orderMapper.toModel(session, TIME_OF_EVENT);

        // Assert
        assertNotNull(order, "Order object should not be null");
        assertEquals(SESSION_ID, order.getCheckoutSessionId(), "Checkout session ID should match");
        assertEquals(BigDecimal.valueOf(12.00), order.getTotalAmount(), "Total amount should match");
        assertEquals(BigDecimal.valueOf(10.00), order.getSubtotalAmount(), "Subtotal amount should match");
        assertEquals(BigDecimal.valueOf(2.00), order.getShippingAmount(), "Shipping amount should match");
        assertEquals(PAYMENT_INTENT_ID, order.getPaymentIntentId(), "Payment intent ID should match");
        assertEquals(TIME_OF_EVENT, order.getCreatedAt(), "Creation time should match");
    }
}