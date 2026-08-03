package com.example.orderservice.dto;

import com.example.orderservice.model.OrderStatus;
import lombok.Data;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class OrderListItemDto {
    private Long id;
    private String customerName;
    private String customerEmail;
    private String status;
    private Long totalCents;
    private String orderDate;
    private String notes;

    public OrderListItemDto(Long id, String customerName, String customerEmail, OrderStatus status, Long totalCents, OffsetDateTime orderDate, String notes) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.status = status.getValue();
        this.totalCents = totalCents;
        this.orderDate = orderDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.notes = notes;
    }
}
