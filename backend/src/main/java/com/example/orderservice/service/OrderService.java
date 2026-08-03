package com.example.orderservice.service;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.model.*;
import com.example.orderservice.repository.AuditLogRepository;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final AuditLogRepository auditLogRepository;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + request.getCustomerId()));

        List<Long> productIds = request.getItems().stream()
                .map(item -> item.getProductId())
                .collect(Collectors.toList());

        List<Product> activeProducts = productRepository.findByIdInAndIsActiveTrue(productIds);

        if (activeProducts.isEmpty()) {
            throw new IllegalArgumentException("No valid products found in the order.");
        }

        Map<Long, Product> activeProductMap = activeProducts.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus("new");

        List<OrderItem> orderItems = request.getItems().stream()
                .filter(item -> activeProductMap.containsKey(item.getProductId()))
                .map(itemRequest -> {
                    Product product = activeProductMap.get(itemRequest.getProductId());
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setOrder(order);
                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setUnitPrice(product.getUnitPrice()); // AC2: Snapshotting unit price
                    return orderItem;
                }).collect(Collectors.toList());

        if (orderItems.isEmpty()) {
            throw new IllegalArgumentException("No valid products found in the order.");
        }

        order.setOrderItems(orderItems);

        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // AC5: Create an audit log entry
        AuditLog auditLog = new AuditLog();
        auditLog.setOrderId(savedOrder.getId());
        auditLog.setStatus(savedOrder.getStatus());
        auditLog.setNotes("Order created");
        auditLogRepository.save(auditLog);

        return savedOrder;
    }
}
