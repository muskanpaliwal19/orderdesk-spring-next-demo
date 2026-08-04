package com.example.catalog.product.controller;

import com.example.catalog.product.Product;
import com.example.catalog.product.exception.ProductNotFoundException;
import com.example.catalog.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;\nimport java.math.BigDecimal;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product createProduct(Long id, String name, String category, String description, BigDecimal price) {
        Product p = new Product(name, description, price, category);
        p.setId(id);
        return p;
    }

    @Test
    void getSimilarProducts_shouldReturnSimilarProducts() throws Exception {
        Product similarProduct = createProduct(2L, "Similar Product", "Test", "Test desc", BigDecimal.valueOf(10.0));
        given(productService.getSimilarProducts(1L)).willReturn(Collections.singletonList(similarProduct));

        mockMvc.perform(get("/api/products/1/similar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2L));
    }

    @Test
    void getSimilarProducts_shouldReturnNotFound_whenProductDoesNotExist() throws Exception {
        given(productService.getSimilarProducts(99L)).willThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/api/products/99/similar"))
                .andExpect(status().isNotFound());
    }
}
