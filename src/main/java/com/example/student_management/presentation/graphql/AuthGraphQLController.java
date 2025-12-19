package com.example.student_management.presentation.graphql;

import com.example.student_management.application.dto.AuthResponse;
import com.example.student_management.application.dto.LoginRequest;
import com.example.student_management.application.dto.RefreshTokenRequest;
import com.example.student_management.application.service.AuthenticationService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

/**
 * GraphQL Controller for Authentication
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthGraphQLController {

    private final AuthenticationService authenticationService;

    @MutationMapping
    public AuthResponse login(@Argument("input") LoginRequest input) {
        AuthResponse response = authenticationService.login(input);
        log.info("Generated Access Token: {}", response.accessToken());
        log.info("Token Type: {}", response.tokenType());
        return response;
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public boolean logout(DataFetchingEnvironment env) {
        // Extract token from request header
        String authHeader = env.getGraphQlContext().get("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return authenticationService.logout(token);
        }
        return false;
    }

    @MutationMapping
    public AuthResponse refreshToken(@Argument("refreshToken") String refreshToken) {
        return authenticationService.refreshToken(new RefreshTokenRequest(refreshToken));
    }
}
