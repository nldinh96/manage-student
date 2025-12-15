package com.example.student_management.infrastructure.security;

import com.example.student_management.domain.model.user.User;
import com.example.student_management.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Custom ReactiveUserDetailsService implementation for Spring Security with WebFlux
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {

    private final JpaUserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromCallable(() -> userRepository.findByUsername(username)
                .map(user -> (UserDetails) new CustomUserDetails(user))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username)));
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return Mono.fromCallable(() -> {
            User existingUser = userRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + user.getUsername()));
            existingUser.updatePassword(newPassword);
            userRepository.save(existingUser);
            return (UserDetails) new CustomUserDetails(existingUser);
        });
    }
}
