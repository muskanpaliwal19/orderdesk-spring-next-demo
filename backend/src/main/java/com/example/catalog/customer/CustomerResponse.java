package com.example.catalog.customer;

import java.time.Instant;

public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private CustomerTier tier;
    private Instant createdAt;

    public CustomerResponse(Long id, String name, String email, CustomerTier tier, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tier = tier;
        this.createdAt = createdAt;
    }

    public static CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), customer.getTier(), customer.getCreatedAt());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public CustomerTier getTier() {
        return tier;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
