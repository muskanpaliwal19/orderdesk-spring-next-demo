package com.example.orders;

import com.example.auditlog.jpa.AuditLog;
import com.example.auditlog.repository.AuditLogRepository;
import org.springframework.data.domain.PageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer customer1;
    private Customer customer2;
    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        orderRepository.deleteAll();
        auditLogRepository.deleteAll();

        customer1 = new Customer("customer1@example.com", "Customer One");
        customerRepository.save(customer1);

        customer2 = new Customer("customer2@example.com", "Customer Two");
        customerRepository.save(customer2);

        order1 = new Order(customer1, Instant.now(), OrderStatus.NEW);
        OrderItem item1 = new OrderItem(order1, "Product A", 1, 1000); // 10.00
        order1.setItems(List.of(item1));

        order2 = new Order(customer2, Instant.now(), OrderStatus.PAID);
        OrderItem item2 = new OrderItem(order2, "Product B", 2, 2500); // 50.00
        order2.setItems(List.of(item2));

        orderRepository.saveAll(List.of(order1, order2));
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        auditLogRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void exportOrders_success() throws Exception {
        mockMvc.perform(get("/api/orders/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"orders.csv\""))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string(containsString("id,customer_email,status,order_date,total_cents")))
                .andExpect(content().string(containsString("\"customer1@example.com\",\"NEW\"")))
                .andExpect(content().string(containsString(",\"1000\"")))
                .andExpect(content().string(containsString("\"customer2@example.com\",\"PAID\"")))
                .andExpect(content().string(containsString(",\"5000\"")));
    }

    @Test
    void exportOrders_withStatusFilter_success() throws Exception {
        mockMvc.perform(get("/api/orders/export").param("status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"orders.csv\""))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string(containsString("id,customer_email,status,order_date,total_cents")))
                .andExpect(content().string(not(containsString("customer1@example.com,NEW"))))
                .andExpect(content().string(not(containsString(",1000"))))
                .andExpect(content().string(containsString("\"customer2@example.com\",\"PAID\"")))
                .andExpect(content().string(containsString(",\"5000\"")));
    }

    @Test
    void exportOrders_emptyResult() throws Exception {
        orderRepository.deleteAll();
        auditLogRepository.deleteAll();

        mockMvc.perform(get("/api/orders/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"orders.csv\""))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string("id,customer_email,status,order_date,total_cents\n"));
    }

    @Test
    void exportOrders_invalidStatus() throws Exception {
        mockMvc.perform(get("/api/orders/export").param("status", "INVALID_STATUS"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void exportOrders_rowCountMatchesTotalOrders() throws Exception {
        // Get the expected row count from the repository
        long expectedRowCount = orderRepository.count();

        mockMvc.perform(get("/api/orders/export"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andExpect(result -> {
                    String csvContent = result.getResponse().getContentAsString();
                    // Split lines and filter out empty lines
                    String[] lines = csvContent.split("\r?\n");
                    long actualRowCount = java.util.Arrays.stream(lines)
                            .filter(line -> !line.trim().isEmpty())
                            .count();

                    // Assert that the number of data rows matches the total orders
                    // (actual rows - 1 for the header)
                    org.assertj.core.api.Assertions.assertThat(actualRowCount - 1).isEqualTo(expectedRowCount);
                });
    }

    @Test
    void createOrder_createsAuditLog() throws Exception {
        long initialAuditLogCount = auditLogRepository.count();

        Order newOrder = new Order(customer1, Instant.now(), OrderStatus.NEW);
        OrderItem item = new OrderItem(newOrder, "Product C", 1, 5000);
        newOrder.setItems(List.of(item));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isOk());

        assertThat(auditLogRepository.count()).isEqualTo(initialAuditLogCount + 1);
        AuditLog auditLog = auditLogRepository.findByOrderByCreatedAtDesc(PageRequest.of(0, 1)).get(0);
        assertThat(auditLog.getEventType()).isEqualTo("ORDER_CREATED");
        assertThat(auditLog.getMessage()).contains("\"orderId\"");
    }

    @Test
    void updateOrderStatus_createsAuditLog() throws Exception {
        long initialAuditLogCount = auditLogRepository.count();
        Long orderId = order1.getId();

        mockMvc.perform(put("/api/orders/" + orderId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"SHIPPED\"}"))
                .andExpect(status().isOk());

        assertThat(auditLogRepository.count()).isEqualTo(initialAuditLogCount + 1);
        AuditLog auditLog = auditLogRepository.findByOrderByCreatedAtDesc(PageRequest.of(0, 1)).get(0);
        assertThat(auditLog.getEventType()).isEqualTo("ORDER_STATUS_UPDATED");
        assertThat(auditLog.getMessage()).contains("\"orderId\":" + orderId);
        assertThat(auditLog.getMessage()).contains("\"newStatus\":\"SHIPPED\"");
    }
}
