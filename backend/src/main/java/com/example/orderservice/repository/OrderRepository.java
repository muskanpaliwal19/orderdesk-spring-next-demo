package com.example.orderservice.repository;

import com.example.orderservice.dto.OrderSummaryDto;
import com.example.orderservice.model.Order;\nimport com.example.orderservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT new com.example.orderservice.dto.OrderSummaryDto(o.id, c.name, o.orderDate, SUM(oi.quantity * oi.unitPriceCents), CAST(o.status AS string)) " +
           "FROM Order o JOIN o.customer c JOIN o.orderItems oi " +
           "GROUP BY o.id, c.name, o.orderDate, o.status " +
           "ORDER BY o.orderDate DESC")
    List<OrderSummaryDto> findAllOrderSummaries();

    @Query("SELECT new com.example.orderservice.dto.OrderSummaryDto(o.id, c.name, o.orderDate, SUM(oi.quantity * oi.unitPriceCents), CAST(o.status AS string)) " +
           "FROM Order o JOIN o.customer c JOIN o.orderItems oi " +
           "WHERE o.status = :status " +
           "GROUP BY o.id, c.name, o.orderDate, o.status " +
           "ORDER BY o.orderDate DESC")
    List<OrderSummaryDto> findOrderSummariesByStatus(@Param("status") OrderStatus status);
}
