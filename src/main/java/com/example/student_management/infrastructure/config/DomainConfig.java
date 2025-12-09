package com.example.student_management.infrastructure.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Domain Layer
 */
@Configuration
@ComponentScan(basePackages = "com.example.student_management.domain")
public class DomainConfig {
}

