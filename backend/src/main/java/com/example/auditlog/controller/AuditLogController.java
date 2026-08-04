package com.example.auditlog.controller;

import com.example.auditlog.controller.dto.AuditLogDto;
import com.example.auditlog.jpa.AuditLog;
import com.example.auditlog.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Autowired
    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AuditLogDto> getRecentAuditLogs() {
        return auditLogService.getRecentAuditLogs().stream()
                .map(AuditLogDto::fromEntity)
                .collect(Collectors.toList());
    }
}
