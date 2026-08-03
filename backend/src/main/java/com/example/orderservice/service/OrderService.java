
package com.example.orderservice.service;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderItemRequest;
import com.example.orderservice.model.Customer;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.Product;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }

        Long customerId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId));

        List<Long> productIds = request.getItems().stream()
                .map(OrderItemRequest::getProductId)
                .toList();

        List<Product> products = productRepository.findByIdInAndIsActiveTrue(productIds);

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(OffsetDateTime.now());
        order.setStatus("CREATED");

        List<OrderItem> orderItems = request.getItems().stream()
                .map(itemRequest -> {
                    Product product = productMap.get(itemRequest.getProductId());
                    if (product == null) {
                        return null; // Will be filtered out later
                    }
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setUnitPrice(product.getPrice()); // Snapshot price
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());

        if (orderItems.isEmpty()) {
            throw new IllegalArgumentException("No active products found to create order.");
        }

        order.setOrderItems(orderItems);

        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        return order;
    }
}
