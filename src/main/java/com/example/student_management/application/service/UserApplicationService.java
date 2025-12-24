package com.example.student_management.application.service;

import com.example.student_management.application.dto.CreateUserRequest;
import com.example.student_management.application.dto.RegisterRequest;
import com.example.student_management.application.dto.UpdateUserRequest;
import com.example.student_management.application.dto.UserDTO;
import com.example.student_management.application.mapper.UserMapper;
import com.example.student_management.domain.model.user.Role;
import com.example.student_management.domain.model.user.User;
import com.example.student_management.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.UUID;

/**
 * Application service for User operations
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserApplicationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Sinks.Many<UserDTO> userRegisteredSink;

    /**
     * Get current authenticated user
     */
    @Transactional(readOnly = true)
    public UserDTO getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Not authenticated");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public UserDTO findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll(Role role) {
        if (role != null) {
            return userRepository.findByRole(role).stream()
                    .map(userMapper::toDTO)
                    .toList();
        }
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    /**
     * Register user - role is optional, defaults to STUDENT
     */
    public UserDTO register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists: " + request.username());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists: " + request.email());
        }

        // Use provided role or default to STUDENT
        Role role = request.role() != null ? request.role() : Role.STUDENT;
        
        // Only ADMIN can register with ADMIN role
        if (role == Role.ADMIN) {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() 
                || !authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                throw new RuntimeException("Only ADMIN can register users with ADMIN role");
            }
        }

        User user = User.create(
                request.username(),
                passwordEncoder.encode(request.password()),
                request.email(),
                role
        );

        UserDTO userDTO = userMapper.toDTO(userRepository.save(user));
        userRegisteredSink.tryEmitNext(userDTO);
        return userDTO;
    }

    /**
     * Admin creates user with specific role
     */
    public UserDTO createUser(CreateUserRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists: " + request.username());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists: " + request.email());
        }

        User user = User.create(
                request.username(),
                passwordEncoder.encode(request.password()),
                request.email(),
                request.role()  // Admin can choose role
        );

        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (request.username() != null && !request.username().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.username())) {
                throw new RuntimeException("Username already exists: " + request.username());
            }
            user.updateUsername(request.username());
        }

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new RuntimeException("Email already exists: " + request.email());
            }
            user.updateEmail(request.email());
        }

        return userMapper.toDTO(userRepository.save(user));
    }

    public boolean changePassword(UUID id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    public UserDTO changeRole(UUID id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.changeRole(role);
        return userMapper.toDTO(userRepository.save(user));
    }

    public boolean enableUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.enable();
        userRepository.save(user);
        return true;
    }

    public boolean disableUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.disable();
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        return true;
    }
}
