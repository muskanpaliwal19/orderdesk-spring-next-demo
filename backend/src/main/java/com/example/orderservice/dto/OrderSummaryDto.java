package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class OrderSummaryDto {
    private Long id;
    private String customerName;
    private OffsetDateTime orderDate;
    private Long totalAmountCents;
    private String status;

    public OrderSummaryDto(Long id, String customerName, OffsetDateTime orderDate, Long totalAmountCents, com.example.orderservice.model.OrderStatus status) {
        this.id = id;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.totalAmountCents = totalAmountCents;
        this.status = status.name();
    }

    public OrderSummaryDto(Long id, String customerName, OffsetDateTime orderDate, Long totalAmountCents, String status) {
        this.id = id;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.totalAmountCents = totalAmountCents;
        this.status = status;
    }
}
