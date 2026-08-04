package com.example.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RevenueReportDto(List<RevenueReportDto.RevenueByDate> revenueByDate, BigDecimal totalRevenue, List<RevenueByStatus> revenueByStatus) {

    public record RevenueByStatus(String status, long orderCount, BigDecimal revenue) {
    }

    public record RevenueByDate(LocalDate date, BigDecimal revenue) {
    }
}
