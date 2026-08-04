package com.example.catalog.customer;

public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private CustomerTier tier;

    public CustomerResponse(Long id, String name, String email, CustomerTier tier) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tier = tier;
    }

    public static CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), customer.getTier());
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
}
