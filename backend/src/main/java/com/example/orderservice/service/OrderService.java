package com.example.orderservice.service;

import com.example.orderservice.dto.OrderSummaryDto;
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

    public List<OrderSummaryDto> getAllOrderSummaries() {
        return orderRepository.findAllOrderSummaries();
    }

    public List<OrderSummaryDto> getOrderSummariesByStatus(String status) {
        return orderRepository.findOrderSummariesByStatus(status);
    }
}
