package com.example.orderservice.dto;

import com.example.orderservice.model.OrderStatus;
import java.math.BigDecimal;

public record RevenueByStatusDto(OrderStatus orderStatus, BigDecimal totalRevenue, Long orderCount) {
    public RevenueByStatusDto(OrderStatus orderStatus, Long orderCount, Long totalRevenueCents) {
        this(orderStatus,
             totalRevenueCents == null ? BigDecimal.ZERO : BigDecimal.valueOf(totalRevenueCents).divide(new BigDecimal("100.00")),
             orderCount);
    }
}
