package com.example.orders;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;
    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        orderRepository.deleteAll();

        customer1 = new Customer("customer1@example.com", "Customer One");
        customerRepository.save(customer1);

        customer2 = new Customer("customer2@example.com", "Customer Two");
        customerRepository.save(customer2);

        order1 = new Order(customer1, Instant.now(), OrderStatus.PENDING);
        OrderItem item1 = new OrderItem(order1, "Product A", 1, 1000); // 10.00
        order1.setItems(List.of(item1));

        order2 = new Order(customer2, Instant.now(), OrderStatus.COMPLETED);
        OrderItem item2 = new OrderItem(order2, "Product B", 2, 2500); // 50.00
        order2.setItems(List.of(item2));

        orderRepository.saveAll(List.of(order1, order2));
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void exportOrders_success() throws Exception {
        mockMvc.perform(get("/api/orders/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"orders.csv\""))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string(containsString("Order ID,Customer Email,Status,Order Date,Total Amount"))) 
                .andExpect(content().string(containsString("customer1@example.com,PENDING")))
                .andExpect(content().string(containsString("10.0")))
                .andExpect(content().string(containsString("customer2@example.com,COMPLETED")))
                .andExpect(content().string(containsString("50.0")));
    }

    @Test
    void exportOrders_withStatusFilter_success() throws Exception {
        mockMvc.perform(get("/api/orders/export").param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"orders.csv\""))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string(containsString("Order ID,Customer Email,Status,Order Date,Total Amount")))
                .andExpect(content().string(not(containsString("customer1@example.com,PENDING"))))
                .andExpect(content().string(not(containsString("10.0"))))
                .andExpect(content().string(containsString("customer2@example.com,COMPLETED")))
                .andExpect(content().string(containsString("50.0")));
    }

    @Test
    void exportOrders_emptyResult() throws Exception {
        orderRepository.deleteAll();

        mockMvc.perform(get("/api/orders/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"orders.csv\""))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string("Order ID,Customer Email,Status,Order Date,Total Amount\n"));
    }

    @Test
    void exportOrders_invalidStatus() throws Exception {
        mockMvc.perform(get("/api/orders/export").param("status", "INVALID_STATUS"))
                .andExpect(status().isBadRequest());
    }
}
