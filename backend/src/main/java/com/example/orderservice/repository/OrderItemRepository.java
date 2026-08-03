package com.example.orderservice.repository;

import com.example.orderservice.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
            SELECT o.status, COUNT(DISTINCT o.id), SUM(oi.unitPriceCents * oi.quantity)
            FROM Order o
            JOIN o.orderItems oi
            GROUP BY o.status
            """)
    List<Object[]> calculateRevenueByStatus();
}
