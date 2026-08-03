package com.example.acme.reports;

import java.util.List;

public record RevenueReportDto(long totalCents, List<RevenueByStatusDto> byStatus) {
}
