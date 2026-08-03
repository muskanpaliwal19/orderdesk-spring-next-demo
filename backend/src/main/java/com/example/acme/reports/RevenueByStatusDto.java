package com.example.acme.reports;

public record RevenueByStatusDto(String status, int orderCount, long totalCents) {
}
