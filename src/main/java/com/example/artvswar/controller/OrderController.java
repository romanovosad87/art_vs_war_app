package com.example.artvswar.controller;

import com.example.artvswar.dto.response.order.OrderResponseDto;
import com.example.artvswar.dto.response.order.OrderShortResponseDto;
import com.example.artvswar.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private static final String SUBJECT = "sub";
    private static final String CREATED_AT = "createdAt";
    private static final int PAGE_SIZE = 50;
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<OrderShortResponseDto>> getAllByAccount(
            @AuthenticationPrincipal Jwt jwt,
            @SortDefault(sort = CREATED_AT, direction = Sort.Direction.DESC)
            @PageableDefault(size = PAGE_SIZE) Pageable pageable) {
        String cognitoSubject = jwt.getClaimAsString(SUBJECT);
        Page<OrderShortResponseDto> dtos = orderService.getAllShortOrdersByAccount(cognitoSubject, pageable);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<OrderResponseDto>> getAllFullByAccount(
            @AuthenticationPrincipal Jwt jwt,
            @PageableDefault(size = 10) Pageable pageable) {
        String cognitoSubject = jwt.getClaimAsString(SUBJECT);
        Page<OrderResponseDto> dtos = orderService.getAllFullOrdersByAccount(cognitoSubject, pageable);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id) {
        OrderResponseDto orderResponseDto = orderService.getOrder(id);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/delivered/{id}")
    public ResponseEntity<Void> setOrderDelivered(@PathVariable Long id) {
        orderService.setOrderDelivered(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
