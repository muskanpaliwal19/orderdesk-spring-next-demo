package com.example.orderservice.dto;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;

public class OrderMapper {

    public static OrderListItemDto toDto(Order order) {
        long totalCents = order.getTotalAmountCents();

        return new OrderListItemDto(
                order.getId(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                order.getStatus(),
                totalCents,
                order.getOrderDate(),
                order.getNotes()
        );
    }
}
