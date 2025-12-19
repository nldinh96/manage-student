package com.example.student_management.infrastructure.security.jwt;

import com.example.student_management.domain.model.user.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT Authentication Filter for Spring MVC (Non-Reactive)
 * Intercepts requests and validates JWT tokens
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Skip JWT for WebSocket handshake
        if ("websocket".equalsIgnoreCase(request.getHeader("Upgrade"))) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);

        if (StringUtils.hasText(token)) {
            // Check if token is blacklisted
            if (tokenBlacklistService.isBlacklisted(token)) {
                log.warn("Token is blacklisted");
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtTokenProvider.validateToken(token)) {
                try {
                    String username = jwtTokenProvider.getUsernameFromToken(token);
                    Role role = jwtTokenProvider.getRoleFromToken(token);

                    // Create authorities from role in JWT
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));

                    // Create authentication token
                    var authentication = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                    );

                    // Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("Authenticated user: {} with role: {}", username, role);

                } catch (Exception e) {
                    log.error("Could not set user authentication: {}", e.getMessage());
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
