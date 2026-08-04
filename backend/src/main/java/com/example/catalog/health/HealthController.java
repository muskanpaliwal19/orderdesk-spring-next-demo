package com.example.catalog.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<java.util.Map<String, String>> healthCheck() {
        return ResponseEntity.ok(java.util.Map.of("status", "UP"));
    }
}
