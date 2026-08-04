package com.example.orders;

public class InvalidStatusException extends IllegalArgumentException {
    public InvalidStatusException(String message) {
        super(message);
    }
}
