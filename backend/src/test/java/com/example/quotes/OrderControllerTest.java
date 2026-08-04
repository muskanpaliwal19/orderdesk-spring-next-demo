
package com.example.quotes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order("John Doe", 100.0, OrderStatus.NEW);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.total").value(100.0))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        Order order = orderRepository.save(new Order("Jane Doe", 200.0, OrderStatus.NEW));

        Map<String, String> body = Map.of("status", "PAID");

        mockMvc.perform(put("/api/orders/{id}/status", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    public void testUpdateOrderStatusInvalidStatus() throws Exception {
        Order order = orderRepository.save(new Order("Jane Doe", 200.0, OrderStatus.NEW));

        Map<String, String> body = Map.of("status", "INVALID");

        mockMvc.perform(put("/api/orders/{id}/status", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateOrderStatusOrderNotFound() throws Exception {
        Map<String, String> body = Map.of("status", "PAID");

        mockMvc.perform(put("/api/orders/{id}/status", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }
}
