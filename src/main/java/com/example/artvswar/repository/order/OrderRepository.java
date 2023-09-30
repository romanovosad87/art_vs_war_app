package com.example.artvswar.repository.order;

import com.example.artvswar.dto.response.order.OrderShortResponseDto;
import com.example.artvswar.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {

   @Query("select new com.example.artvswar.dto.response.order.OrderShortResponseDto(o.id, o.totalAmount, o.createdAt) from Order o "
           + "where o.account.cognitoSubject = ?1")
   Page<OrderShortResponseDto> findAllByAccount_CognitoSubject(String cognitoSubject, Pageable pageable);

   Optional<Order> findByCheckoutSessionId(String checkoutSessionId);
}
