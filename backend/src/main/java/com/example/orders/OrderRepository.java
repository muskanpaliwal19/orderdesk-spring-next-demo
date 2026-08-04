
package com.example.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orders.dto.OrderExportRow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT NEW com.example.orders.dto.OrderExportRow(
                o.id,
                o.customer.email,
                o.status,
                o.orderDate,
                (SELECT COALESCE(SUM(oi.priceCents * oi.quantity), 0) FROM OrderItem oi WHERE oi.order = o)
            )
            FROM Order o
            WHERE (:status IS NULL OR o.status = :status)
            ORDER BY o.id ASC
            """)
    List<OrderExportRow> findOrdersForExport(@Param("status") OrderStatus status);
}

