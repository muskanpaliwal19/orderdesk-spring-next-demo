package com.example.catalog.product.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/test-data.sql")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenGetProducts_thenReturnsActiveProductsSortedByName() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[0].sku").value("SKU1"))
                .andExpect(jsonPath("$[0].price").value(19.99))
                .andExpect(jsonPath("$[1].name").value("Product B"))
                .andExpect(jsonPath("$[1].sku").value("SKU2"))
                .andExpect(jsonPath("$[1].price").value(29.99))
                .andExpect(jsonPath("$[2].name").value("Product C"))
                .andExpect(jsonPath("$[2].sku").value("SKU3"))
                .andExpect(jsonPath("$[2].price").value(40.00))
                .andExpect(jsonPath("$[3].name").value("Product D"))
                .andExpect(jsonPath("$[3].sku").value("SKU4"))
                .andExpect(jsonPath("$[3].price").value(49.94));
    }
}
