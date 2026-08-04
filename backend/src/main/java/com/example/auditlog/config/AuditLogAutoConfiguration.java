package com.example.auditlog.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.example.auditlog")
@EnableJpaRepositories("com.example.auditlog.repository")
@EntityScan("com.example.auditlog.jpa")
public class AuditLogAutoConfiguration {
}
