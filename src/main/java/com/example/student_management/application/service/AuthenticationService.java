package com.example.student_management.application.service;

import com.example.student_management.application.dto.AuthResponse;
import com.example.student_management.application.dto.LoginRequest;
import com.example.student_management.application.dto.RefreshTokenRequest;
import com.example.student_management.domain.model.user.User;
import com.example.student_management.domain.repository.UserRepository;
import com.example.student_management.infrastructure.security.jwt.JwtProperties;
import com.example.student_management.infrastructure.security.jwt.JwtTokenProvider;
import com.example.student_management.infrastructure.security.jwt.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication Service - handles login, logout and token refresh
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * Authenticate user and return tokens
     */
    public AuthResponse login(LoginRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // Verify password
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Check if user is enabled
        if (!user.isEnabled()) {
            throw new RuntimeException("User account is disabled");
        }

        // Generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        log.info("User {} logged in successfully", user.getUsername());

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                (int) (jwtProperties.getAccessTokenExpiration() / 1000),
                user.getId().toString(),
                user.getUsername(),
                user.getRole(),
                decodeToken(accessToken),
                decodeToken(refreshToken)
        );
    }

    /**
     * Refresh access token using refresh token
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        // Validate refresh token
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // Get user ID from refresh token
        var userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is still enabled
        if (!user.isEnabled()) {
            throw new RuntimeException("User account is disabled");
        }

        // Generate new access token (role is fetched fresh from DB)
        String newAccessToken = jwtTokenProvider.generateAccessToken(
                user.getId(),
                user.getUsername(),
                user.getRole()  // Fresh role from DB!
        );

        log.info("Token refreshed for user: {}", user.getUsername());

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                "Bearer",
                (int) (jwtProperties.getAccessTokenExpiration() / 1000),
                user.getId().toString(),
                user.getUsername(),
                user.getRole(),
                decodeToken(newAccessToken),
                decodeToken(refreshToken)
        );
    }

    /**
     * Logout user by blacklisting the token
     */
    public boolean logout(String token) {
        if (token != null && !token.isEmpty()) {
            tokenBlacklistService.blacklistToken(token);
            log.info("User logged out successfully");
            return true;
        }
        return false;
    }

    private String decodeToken(String token) {
        try {
            var claims = jwtTokenProvider.parseClaims(token);
            return claims.toString();
        } catch (Exception e) {
            return "Invalid token";
        }
    }
}
