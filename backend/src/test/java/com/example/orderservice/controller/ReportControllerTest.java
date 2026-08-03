package com.example.orderservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/test-data.sql")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRevenueReport() throws Exception {
        mockMvc.perform(get("/api/reports/revenue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCents").value(13992))
                .andExpect(jsonPath("$.byStatus.length()").value(3))
                .andExpect(jsonPath("$.byStatus[?(@.status == 'NEW')].orderCount").value(1))
                .andExpect(jsonPath("$.byStatus[?(@.status == 'NEW')].totalCents").value(1999))
                .andExpect(jsonPath("$.byStatus[?(@.status == 'SHIPPED')].orderCount").value(1))
                .andExpect(jsonPath("$.byStatus[?(@.status == 'SHIPPED')].totalCents").value(8994))
                .andExpect(jsonPath("$.byStatus[?(@.status == 'PAID')].orderCount").value(1))
                .andExpect(jsonPath("$.byStatus[?(@.status == 'PAID')].totalCents").value(2999));
    }
}
