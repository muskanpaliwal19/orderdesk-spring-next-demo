package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderListItemDto;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderListItemDto> getOrders(@RequestParam(required = false) OrderStatus status) {
        if (status != null) {
            return orderService.findOrdersByStatus(status);
        } else {
            return orderService.findAllOrders();
        }
    }
}
