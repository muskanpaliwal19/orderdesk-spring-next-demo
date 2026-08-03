package com.example.orderservice.service;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderItemRequest;
import com.example.orderservice.dto.OrderListItemDto;
import com.example.orderservice.event.OrderCreatedEvent;
import com.example.orderservice.model.Customer;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.model.Product;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + request.getCustomerId()));

        List<Long> productIds = request.getItems().stream()
                .map(OrderItemRequest::getProductId)
                .toList();

        List<Product> activeProducts = productRepository.findByIdInAndIsActiveTrue(productIds);

        if (activeProducts.size() != request.getItems().size()) {
            throw new IllegalArgumentException("One or more products are inactive or do not exist.");
        }

        Map<Long, Product> productMap = activeProducts.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(OffsetDateTime.now());
        order.setStatus(OrderStatus.NEW);
        order.setNotes(request.getNotes());

        List<OrderItem> orderItems = request.getItems().stream()
                .map(itemRequest -> {
                    Product product = productMap.get(itemRequest.getProductId());
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setUnitPriceCents(product.getPriceCents()); // Snapshot price
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        BigDecimal totalAmount = orderItems.stream()
                .map(item -> BigDecimal.valueOf(item.getUnitPriceCents()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmountCents(totalAmount.intValue());

        Order savedOrder = orderRepository.save(order);

        eventPublisher.publishEvent(new OrderCreatedEvent(this, savedOrder));

        return savedOrder;
    }

    public List<OrderListItemDto> findAllOrders() {
        return orderRepository.findAllOrdersWithTotals();
    }

    public List<OrderListItemDto> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findOrdersByStatusWithTotals(status);
    }
}
