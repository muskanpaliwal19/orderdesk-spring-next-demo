package com.example.orderservice.repository;

import com.example.orderservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIdInAndIsActiveTrue(List<Long> ids);
}
