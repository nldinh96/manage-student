package com.example.student_management.domain.repository;

import com.example.student_management.domain.model.user.Role;
import com.example.student_management.domain.model.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User aggregate
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> findByRole(Role role);

    boolean existsById(UUID id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void deleteById(UUID id);
}
