package com.example.catalog.product.controller;

import com.example.catalog.product.Product;
import com.example.catalog.product.dto.ProductDto;
import com.example.catalog.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}/similar")
    public ResponseEntity<List<ProductDto>> getSimilarProducts(@PathVariable Long id) {
        List<Product> similarProducts = productService.getSimilarProducts(id);
        return ResponseEntity.ok(similarProducts.stream().map(this::toDto).collect(Collectors.toList()));
    }

    private ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory());
    }
}
