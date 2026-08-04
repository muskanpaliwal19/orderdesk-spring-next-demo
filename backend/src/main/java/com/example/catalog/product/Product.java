package com.example.catalog.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer unitPriceCents;
    private String category;

    public Product() {
    }

    public Product(String name, String description, Integer unitPriceCents, String category) {
        this.name = name;
        this.description = description;
        this.unitPriceCents = unitPriceCents;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getUnitPriceCents() {
        return unitPriceCents;
    }

    public void setUnitPriceCents(Integer unitPriceCents) {
        this.unitPriceCents = unitPriceCents;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
