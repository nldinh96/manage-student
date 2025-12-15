package com.example.student_management.application.mapper;

import com.example.student_management.application.dto.UserDTO;
import com.example.student_management.domain.model.user.User;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * Mapper for User entity to DTO conversion
 */
@Component
public class UserMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt().format(FORMATTER),
                user.getUpdatedAt() != null ? user.getUpdatedAt().format(FORMATTER) : null
        );
    }
}

