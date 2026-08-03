package com.example.orderservice.dto;

import com.example.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryDto {
    private Long id;
    private String customerName;
    private OffsetDateTime orderDate;
    private Long totalAmountCents;
    private String status;

    public OrderSummaryDto(Long id, String customerName, OffsetDateTime orderDate, Long totalAmountCents, OrderStatus status) {
        this.id = id;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.totalAmountCents = totalAmountCents;
        this.status = status.name();
    }
}
