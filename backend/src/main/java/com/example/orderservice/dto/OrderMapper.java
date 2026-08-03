
package com.example.orderservice.dto;

import com.example.orderservice.model.Order;

public class OrderMapper {

    public static OrderListItemDto toDto(Order order) {
        return new OrderListItemDto(
                order.getId(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                order.getStatus(),
                order.getTotalAmountCents().longValue(),
                order.getOrderDate(),
                order.getNotes()
        );
    }
}
