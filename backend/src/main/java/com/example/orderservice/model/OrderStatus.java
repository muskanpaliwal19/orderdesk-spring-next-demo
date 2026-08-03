package com.example.orderservice.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    NEW("new"),
    PAID("paid"),
    SHIPPED("shipped"),
    CANCELLED("cancelled");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
