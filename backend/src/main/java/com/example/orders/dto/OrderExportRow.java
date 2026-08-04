package com.example.orders.dto;

import com.example.orders.OrderStatus;

import java.time.Instant;

public record OrderExportRow(
    Long id,
    String customerEmail,
    OrderStatus status,
    Instant orderDate,
    Long totalCents
) {}
