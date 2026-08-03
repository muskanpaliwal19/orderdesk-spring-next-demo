package com.example.acme.reports;

import com.example.acme.reports.ReportService;
import com.example.acme.reports.RevenueReportDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/api/reports/revenue")
    public RevenueReportDto getRevenueReport() {
        return reportService.getRevenueReport();
    }
}
