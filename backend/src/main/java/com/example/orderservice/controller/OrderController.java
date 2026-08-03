package com.example.orderservice.controller;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.OrderResponse;\nimport com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.orderservice.dto.OrderSummaryDto;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private static final Set<String> ALLOWED_STATUSES = Set.of("new", "paid", "shipped", "cancelled");

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order createdOrder = orderService.createOrder(request);
        // Map entity to DTO
        OrderResponse orderResponse = OrderResponse.from(createdOrder);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderSummaryDto>> getOrders(@RequestParam(required = false) String status) {
        if (status != null && !ALLOWED_STATUSES.contains(status.toLowerCase())) {
            throw new IllegalArgumentException("Invalid status value. Allowed values are: new, paid, shipped, cancelled.");
        }
        List<OrderSummaryDto> orders = orderService.findAll(Optional.ofNullable(status));
        return ResponseEntity.ok(orders);
    }
}
