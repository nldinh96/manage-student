package com.example.student_management.presentation.graphql;

import com.example.student_management.application.dto.RegisterRequest;
import com.example.student_management.application.dto.UpdateUserRequest;
import com.example.student_management.application.dto.UserDTO;
import com.example.student_management.application.service.UserApplicationService;
import com.example.student_management.domain.model.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

/**
 * GraphQL Controller for User operations
 */
@Controller
@RequiredArgsConstructor
public class UserGraphQLController {

    private final UserApplicationService userApplicationService;

    // ==================== Queries ====================

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO user(@Argument String id) {
        return userApplicationService.findById(UUID.fromString(id));
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO userByUsername(@Argument String username) {
        return userApplicationService.findByUsername(username);
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> users(@Argument Role role) {
        return userApplicationService.findAll(role);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public UserDTO me() {
        return userApplicationService.getCurrentUser();
    }

    // ==================== Mutations ====================

    /**
     * Register user - role is optional, defaults to STUDENT
     */
    @MutationMapping
    public UserDTO register(@Argument("input") RegisterRequest input) {
        return userApplicationService.register(input);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @userApplicationService.findById(T(java.util.UUID).fromString(#id)).username()")
    public UserDTO updateUser(@Argument String id, @Argument("input") UpdateUserRequest input) {
        return userApplicationService.updateUser(UUID.fromString(id), input);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @userApplicationService.findById(T(java.util.UUID).fromString(#id)).username()")
    public boolean changePassword(@Argument String id, @Argument String newPassword) {
        return userApplicationService.changePassword(UUID.fromString(id), newPassword);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO changeRole(@Argument String id, @Argument Role role) {
        return userApplicationService.changeRole(UUID.fromString(id), role);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public boolean enableUser(@Argument String id) {
        return userApplicationService.enableUser(UUID.fromString(id));
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public boolean disableUser(@Argument String id) {
        return userApplicationService.disableUser(UUID.fromString(id));
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteUser(@Argument String id) {
        return userApplicationService.deleteUser(UUID.fromString(id));
    }
}
