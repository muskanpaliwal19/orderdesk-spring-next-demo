package com.example.orderservice.service;

import com.example.orderservice.dto.RevenueReportDto;
import com.example.orderservice.dto.RevenueByStatusDto;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final OrderItemRepository orderItemRepository;

    public ReportService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public RevenueReportDto getRevenueReport() {
        List<Object[]> results = orderItemRepository.calculateRevenueByStatus();
        List<RevenueByStatusDto> revenueByStatus = results.stream()
                .map(result -> {
                    OrderStatus status = (OrderStatus) result[0];
                    long orderCount = ((Number) result[1]).longValue();
                    BigDecimal totalRevenueCents = result[2] == null ? BigDecimal.ZERO : new BigDecimal(((Number) result[2]).toString());
                    BigDecimal totalRevenue = totalRevenueCents.divide(new BigDecimal(100));
                    return new RevenueByStatusDto(status, orderCount, totalRevenue);
                })
                .collect(Collectors.toList());

        return new RevenueReportDto(revenueByStatus);
    }
}
