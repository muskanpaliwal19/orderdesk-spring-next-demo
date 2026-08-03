package com.example.orderservice.dto;

import com.example.orderservice.model.OrderItem;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class OrderItemResponse {
    Long id;
    Long productId;
    Integer quantity;
    BigDecimal unitPrice;

    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProduct().getId(),
                orderItem.getQuantity(),
                new BigDecimal(orderItem.getUnitPriceCents()).movePointLeft(2)
        );
    }
}
