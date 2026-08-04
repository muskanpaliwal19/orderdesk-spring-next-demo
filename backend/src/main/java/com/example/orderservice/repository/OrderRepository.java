
package com.example.orderservice.repository;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT CAST(o.order_date AS DATE) as order_day, SUM(o.total_amount_cents) as daily_revenue FROM orders o GROUP BY CAST(o.order_date AS DATE)", nativeQuery = true)
    List<Object[]> findDailyRevenue();

    @Query(value = "SELECT o.status, COUNT(o.id), SUM(o.total_amount_cents) FROM orders o GROUP BY o.status", nativeQuery = true)
    List<Object[]> findRevenueByStatus();

    @Query("SELECT o FROM Order o JOIN FETCH o.customer ORDER BY o.orderDate DESC")
    List<Order> findAllOrdersWithTotals();

    @Query("SELECT o FROM Order o JOIN FETCH o.customer WHERE o.status = :status ORDER BY o.orderDate DESC")
    List<Order> findOrdersByStatusWithTotals(OrderStatus status);
}

