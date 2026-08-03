package com.example.acme.reports;

import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

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
        List<RevenueByStatusDto> byStatus = results.stream()
                .map(result -> new RevenueByStatusDto(
                        ((OrderStatus) result[0]).name(),
                        ((Number) result[1]).intValue(),
                        ((Number) result[2]).longValue()))
                .collect(Collectors.toList());

        long totalCents = byStatus.stream()
                .mapToLong(RevenueByStatusDto::totalCents)
                .sum();

        return new RevenueReportDto(totalCents, byStatus);
    }
}
