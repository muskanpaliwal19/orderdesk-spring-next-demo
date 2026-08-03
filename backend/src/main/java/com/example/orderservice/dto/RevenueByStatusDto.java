package com.example.orderservice.dto;

import com.example.orderservice.model.OrderStatus;

import java.math.BigDecimal;

public record RevenueByStatusDto(OrderStatus status, long orderCount, BigDecimal totalRevenue) {
}
