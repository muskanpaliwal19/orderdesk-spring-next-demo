package com.example.catalog.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;\nimport jakarta.validation.constraints.NotNull;

public class CreateCustomerRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull(message = \"Tier is mandatory\")\n    private CustomerTier tier;

    public CustomerTier getTier() {
        return tier;
    }

    public void setTier(CustomerTier tier) {
        this.tier = tier;
    }
}
