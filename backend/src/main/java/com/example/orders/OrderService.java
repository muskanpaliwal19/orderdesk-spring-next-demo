
package com.example.orders;

import com.example.audit.AuditLog;
import com.example.audit.AuditLogRepository;

import com.example.orders.dto.OrderExportRow;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AuditLogRepository auditLogRepository;

    public OrderService(OrderRepository orderRepository, AuditLogRepository auditLogRepository) {
        this.orderRepository = orderRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        auditLogRepository.save(new AuditLog(id, status.toString(), "status_changed"));
        return orderRepository.save(order);
    }

    public List<OrderExportRow> findOrdersForExport(OrderStatus status) {
        return orderRepository.findOrdersForExport(status);
    }
}

