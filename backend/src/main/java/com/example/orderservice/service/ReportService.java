package com.example.orderservice.service;

import com.example.orderservice.dto.RevenueByStatusDto;
import com.example.orderservice.dto.RevenueReportDto;
import com.example.orderservice.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final OrderItemRepository orderItemRepository;

    public ReportService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public RevenueReportDto getRevenueReport() {
        List<RevenueByStatusDto> revenueByStatus = orderItemRepository.calculateRevenueByStatus();
        return new RevenueReportDto(revenueByStatus);
    }
}
