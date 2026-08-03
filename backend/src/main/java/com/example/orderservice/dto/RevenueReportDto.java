package com.example.orderservice.dto;

import java.util.List;

public record RevenueReportDto(List<RevenueByStatusDto> revenueByStatus) {
}
