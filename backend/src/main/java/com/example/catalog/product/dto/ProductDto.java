package com.example.catalog.product.dto;

import java.math.BigDecimal;

public record ProductDto(
    Long id,
    String name,
    String sku,
    String description,
    BigDecimal price,
    String category
) {}
