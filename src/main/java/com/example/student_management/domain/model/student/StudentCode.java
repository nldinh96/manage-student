package com.example.student_management.domain.model.student;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object representing Student Code
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentCode {

    @Column(name = "student_code", unique = true, nullable = false, length = 20)
    private String value;

    public StudentCode(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Student code cannot be empty");
        }
        if (!value.matches("^[A-Z0-9]{6,20}$")) {
            throw new IllegalArgumentException("Student code must be 6-20 characters of uppercase letters and numbers");
        }
        this.value = value.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return value;
    }
}
