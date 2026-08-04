package com.example.audit;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private String newStatus;

    private String eventType;

    private LocalDateTime timestamp;

    public AuditLog() {
    }

    public AuditLog(Long orderId, String newStatus, String eventType) {
        this.orderId = orderId;
        this.newStatus = newStatus;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
}
