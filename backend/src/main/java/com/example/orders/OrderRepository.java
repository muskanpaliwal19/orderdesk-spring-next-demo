
package com.example.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orders.dto.OrderExportRow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT NEW com.example.orders.dto.OrderExportRow(
                o.id,
                o.customer.email,
                o.status,
                o.orderDate,
                COALESCE(SUM(oi.priceCents * oi.quantity), 0)
            )
            FROM Order o
            LEFT JOIN o.items oi
            WHERE (:status IS NULL OR o.status = :status)
            GROUP BY o.id, o.customer.email, o.status, o.orderDate
            ORDER BY o.id ASC
            """)
    List<OrderExportRow> findOrdersForExport(@Param("status") OrderStatus status);
}

