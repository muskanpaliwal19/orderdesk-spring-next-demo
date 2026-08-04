package com.example.catalog.product.service;

import com.example.catalog.product.Product;
import com.example.catalog.product.dto.ProductDto;
import com.example.catalog.product.exception.ProductNotFoundException;
import com.example.catalog.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getProducts() {
        return productRepository.findAllByIsActiveTrueOrderByNameAsc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProductDto convertToDto(Product product) {
        BigDecimal price = BigDecimal.valueOf(product.getUnitPriceCents()).divide(BigDecimal.valueOf(100));
        return new ProductDto(
                product.getId(),
                product.getName(),\n                product.getSku(),
                product.getDescription(),
                price,
                product.getCategory()
        );
    }
}
