
package com.example.orders;

import com.example.auditlog.service.AuditLogService;
import com.example.orders.dto.OrderExportRow;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AuditLogService auditLogService;

    public OrderService(OrderRepository orderRepository, AuditLogService auditLogService) {
        this.orderRepository = orderRepository;
        this.auditLogService = auditLogService;
    }

    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        auditLogService.logEvent("ORDER_CREATED", Map.of("orderId", savedOrder.getId()));
        return savedOrder;
    }

    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        auditLogService.logEvent("ORDER_STATUS_UPDATED", Map.of("orderId", id, "newStatus", status.toString()));
        return updatedOrder;
    }

    public List<OrderExportRow> findOrdersForExport(OrderStatus status) {
        return orderRepository.findOrdersForExport(status);
    }

    public void exportOrdersToCsv(PrintWriter writer, OrderStatus status) {
        List<OrderExportRow> orders = orderRepository.findOrdersForExport(status);
        writer.println("Order ID,Customer Email,Status,Order Date,Total Amount");

        for (OrderExportRow order : orders) {
            writer.println(
                    order.id() + "," +
                            order.customerEmail() + "," +
                            order.status() + "," +
                            order.orderDate() + "," +
                            order.totalCents()
            );
        }
    }
}
