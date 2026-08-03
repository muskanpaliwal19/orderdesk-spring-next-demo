package com.example.orderservice.dto;

import com.example.orderservice.model.Order;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class OrderResponse {
    Long id;
    Long customerId;
    OffsetDateTime orderDate;
    BigDecimal totalAmount;
    String status;
    List<OrderItemResponse> orderItems;

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderItems().stream()
                        .map(OrderItemResponse::from)
                        .collect(Collectors.toList())
        );
    }
}
