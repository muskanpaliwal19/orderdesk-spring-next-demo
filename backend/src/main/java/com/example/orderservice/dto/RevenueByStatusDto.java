package com.example.orderservice.dto;

public record RevenueByStatusDto(String status, long orderCount, long totalCents) {
}
