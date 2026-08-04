package com.example.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RevenueReportDto(List<RevenueReportDto.RevenueByDate> revenueByDate, BigDecimal totalRevenue) {

    public record RevenueByDate(LocalDate date, BigDecimal revenue) {
    }
}
