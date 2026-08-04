package com.example.orders;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToOrderStatusConverter implements Converter<String, OrderStatus> {

    @Override
    public OrderStatus convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            return OrderStatus.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusException("Invalid status: " + source);
        }
    }
}
