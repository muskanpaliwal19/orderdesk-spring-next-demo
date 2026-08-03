package com.example.orderservice.repository;

import com.example.orderservice.dto.OrderListItemDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT new com.example.orderservice.dto.OrderListItemDto(" +
            "o.id, " +
            "COALESCE(c.name, 'Deleted Customer'), " +
            "COALESCE(c.email, ''), " +
            "o.status, " +
            "COALESCE(SUM(oi.quantity * oi.unitPriceCents), 0L), " +
            "o.orderDate, " +
            "o.notes) " +
            "FROM Order o LEFT JOIN o.customer c LEFT JOIN o.orderItems oi " +
            "GROUP BY o.id, c.name, c.email, o.status, o.orderDate, o.notes " +
            "ORDER BY o.orderDate DESC")
    List<OrderListItemDto> findAllOrdersWithTotals();

    @Query("SELECT new com.example.orderservice.dto.OrderListItemDto(" +
            "o.id, " +
            "COALESCE(c.name, 'Deleted Customer'), " +
            "COALESCE(c.email, ''), " +
            "o.status, " +
            "COALESCE(SUM(oi.quantity * oi.unitPriceCents), 0L), " +
            "o.orderDate, " +
            "o.notes) " +
            "FROM Order o LEFT JOIN o.customer c LEFT JOIN o.orderItems oi " +
            "WHERE o.status = :status " +
            "GROUP BY o.id, c.name, c.email, o.status, o.orderDate, o.notes " +
            "ORDER BY o.orderDate DESC")
    List<OrderListItemDto> findOrdersByStatusWithTotals(@Param("status") OrderStatus status);
}

