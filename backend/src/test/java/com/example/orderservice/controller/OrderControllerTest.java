package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderListItemDto;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser
    public void getOrders_noStatus_returnsAllOrders() throws Exception {
        OrderListItemDto order = new OrderListItemDto(1L, "Test Customer", "test@test.com", OrderStatus.NEW, 10000L, OffsetDateTime.now(), "notes");

        when(orderService.findAllOrders()).thenReturn(List.of(order));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].customerName").value("Test Customer"));
    }

    @Test
    @WithMockUser
    public void getOrders_withValidStatus_returnsFilteredOrders() throws Exception {
        OrderListItemDto order = new OrderListItemDto(1L, "Test Customer", "test@test.com", OrderStatus.NEW, 10000L, OffsetDateTime.now(), "notes");

        when(orderService.findOrdersByStatus(OrderStatus.NEW)).thenReturn(List.of(order));

        mockMvc.perform(get("/api/orders").param("status", "new"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("new"));
    }

    @Test
    @WithMockUser
    public void getOrders_withInvalidStatus_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/orders").param("status", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void getOrders_withValidStatus_returnsEmptyList() throws Exception {
        when(orderService.findOrdersByStatus(OrderStatus.SHIPPED)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/orders").param("status", "shipped"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
