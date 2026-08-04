
package com.example.orderservice.service;

import com.example.orderservice.dto.RevenueReportDto;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final OrderRepository orderRepository;

    @Autowired
    public ReportServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public RevenueReportDto getRevenueReport() {
        List<Object[]> dailyRevenueResults = orderRepository.findDailyRevenue();
        List<RevenueReportDto.RevenueByDate> revenueByDateList = dailyRevenueResults.stream()
                .map(result -> {
                    BigDecimal revenueInCents = (BigDecimal) result[1];
                    BigDecimal revenueInDollars = revenueInCents.divide(new BigDecimal(100));
                    return new RevenueReportDto.RevenueByDate(((java.sql.Date) result[0]).toLocalDate(), revenueInDollars);
                })
                .collect(Collectors.toList());

        BigDecimal totalRevenue = revenueByDateList.stream()
                .map(RevenueReportDto.RevenueByDate::revenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Object[]> revenueByStatusResults = orderRepository.findRevenueByStatus();
        List<RevenueReportDto.RevenueByStatus> revenueByStatusList = revenueByStatusResults.stream()
                .map(result -> {
                    BigDecimal revenueInCents = (BigDecimal) result[2];
                    BigDecimal revenueInDollars = revenueInCents.divide(new BigDecimal(100));
                    return new RevenueReportDto.RevenueByStatus((String) result[0], ((Number) result[1]).longValue(), revenueInDollars);
                })
                .collect(Collectors.toList());

        return new RevenueReportDto(revenueByDateList, totalRevenue, revenueByStatusList);
    }
}
