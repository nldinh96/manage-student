package com.example.student_management.application.dto;

import com.example.student_management.domain.model.user.Role;

/**
 * DTO for User response
 */
public record UserDTO(
        String id,
        String username,
        String email,
        Role role,
        boolean enabled,
        String createdAt,
        String updatedAt
) {}

