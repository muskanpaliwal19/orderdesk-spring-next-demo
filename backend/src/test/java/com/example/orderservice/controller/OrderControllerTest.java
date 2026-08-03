package com.example.orderservice.controller;

import com.example.orderservice.config.SecurityConfig;
import com.example.orderservice.dto.OrderSummaryDto;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class, 
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    })
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser
    void getOrders_noStatus_shouldReturnOk() throws Exception {
        OrderSummaryDto orderSummary = new OrderSummaryDto(1L, "Test Customer", OffsetDateTime.now(), 100L, "CREATED");
        when(orderService.findAll(Optional.empty())).thenReturn(List.of(orderSummary));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerName").value("Test Customer"));
    }

    @Test
    @WithMockUser
    void getOrders_withValidStatus_shouldReturnOk() throws Exception {
        when(orderService.findAll(Optional.of("new"))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/orders?status=new"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser
    void getOrders_withInvalidStatus_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/orders?status=invalid"))
                .andExpect(status().isBadRequest());
    }
}
