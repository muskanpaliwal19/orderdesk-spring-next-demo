
package com.example.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RevenueReportDto {

    private List<RevenueByDate> revenueByDate;
    private BigDecimal totalRevenue;

    public RevenueReportDto(List<RevenueByDate> revenueByDate, BigDecimal totalRevenue) {
        this.revenueByDate = revenueByDate;
        this.totalRevenue = totalRevenue;
    }

    public List<RevenueByDate> getRevenueByDate() {
        return revenueByDate;
    }

    public void setRevenueByDate(List<RevenueByDate> revenueByDate) {
        this.revenueByDate = revenueByDate;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public static class RevenueByDate {
        private LocalDate date;
        private BigDecimal revenue;

        public RevenueByDate(LocalDate date, BigDecimal revenue) {
            this.date = date;
            this.revenue = revenue;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }
}

