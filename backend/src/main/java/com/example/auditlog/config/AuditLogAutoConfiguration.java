package com.example.auditlog.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.example.auditlog")
@EnableJpaRepositories("com.example.auditlog.repository")
public class AuditLogAutoConfiguration {
}
