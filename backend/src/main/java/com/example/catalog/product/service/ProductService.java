package com.example.catalog.product.service;

import com.example.catalog.product.Product;
import com.example.catalog.product.controller.ProductDto;
import com.example.catalog.product.exception.ProductNotFoundException;
import com.example.catalog.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getSimilarProducts(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        return productRepository.findByCategoryAndIdNot(product.getCategory(), id).stream()
                .map(p -> new ProductDto(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getCategory()))
                .collect(Collectors.toList());
    }
}
