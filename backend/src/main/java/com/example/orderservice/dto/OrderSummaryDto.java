package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class OrderSummaryDto {
    private Long id;
    private String customerName;
    private OffsetDateTime orderDate;
    private Long totalAmountCents;
    private String status;
}
