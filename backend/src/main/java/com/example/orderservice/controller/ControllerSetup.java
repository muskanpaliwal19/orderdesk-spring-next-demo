package com.example.orderservice.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class ControllerSetup {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Treat empty strings as null for all string parameters
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
