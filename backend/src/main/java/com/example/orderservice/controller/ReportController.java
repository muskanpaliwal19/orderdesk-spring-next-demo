package com.example.orderservice.controller;

import com.example.orderservice.dto.RevenueReportDto;
import com.example.orderservice.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/revenue")
    public RevenueReportDto getRevenueReport() {
        return reportService.getRevenueReport();
    }
}
