package com.example.catalog.product.service;

import com.example.catalog.product.Product;
import com.example.catalog.product.exception.ProductNotFoundException;
import com.example.catalog.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;\nimport java.math.BigDecimal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product createProduct(Long id, String name, String category, String description, BigDecimal price) {
        Product p = new Product(name, description, price, category);
        p.setId(id);
        return p;
    }

    @Test
    void getSimilarProducts_shouldReturnSimilarProducts() {
        Product product1 = createProduct(1L, "Apple", "Fruit", "A red fruit", BigDecimal.valueOf(1.0));
        Product product2 = createProduct(2L, "Banana", "Fruit", "A yellow fruit", BigDecimal.valueOf(0.5));
        Product product3 = createProduct(3L, "Carrot", "Vegetable", "An orange vegetable", BigDecimal.valueOf(0.3));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findByCategoryAndIdNot("Fruit", 1L)).thenReturn(Arrays.asList(product2));

        List<Product> similarProducts = productService.getSimilarProducts(1L);

        assertNotNull(similarProducts);
        assertEquals(1, similarProducts.size());
        assertEquals(2L, similarProducts.get(0).getId());
    }

    @Test
    void getSimilarProducts_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.getSimilarProducts(1L);
        });
    }

    @Test
    void getSimilarProducts_shouldReturnEmptyList_whenNoSimilarProducts() {
        Product product1 = createProduct(1L, "UniqueProduct", "UniqueType", "A very unique description", BigDecimal.valueOf(1.0));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findByCategoryAndIdNot("UniqueType", 1L)).thenReturn(Collections.emptyList());

        List<Product> similarProducts = productService.getSimilarProducts(1L);

        assertNotNull(similarProducts);
        assertTrue(similarProducts.isEmpty());
    }
}
