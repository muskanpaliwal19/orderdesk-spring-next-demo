package com.example.orderservice.service;

import com.example.orderservice.dto.OrderListItemDto;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderListItemDto> findAllOrders() {
        return orderRepository.findAllOrdersWithTotals();
    }

    public List<OrderListItemDto> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findOrdersByStatusWithTotals(status);
    }
}
