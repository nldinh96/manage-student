package com.example.student_management.application.dto;

import com.example.student_management.domain.model.user.Role;

/**
 * Response DTO for authentication (login/refresh)
 */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        int expiresIn,
        String userId,
        String username,
        Role role,
        String decodedAccessToken,
        String decodedRefreshToken
) {
    public AuthResponse(String accessToken, String refreshToken, long expiresIn,
                        String userId, String username, Role role) {
        this(accessToken, refreshToken, "Bearer", (int) expiresIn, userId, username, role, "", "");
    }
}
