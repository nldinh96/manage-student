package com.example.student_management.domain.service;

import com.example.student_management.domain.model.student.StudentCode;
import com.example.student_management.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Domain Service for Student business logic that doesn't belong to a single entity
 */
@Service
@RequiredArgsConstructor
public class StudentDomainService {

    private final StudentRepository studentRepository;

    public void validateUniqueStudentCode(StudentCode studentCode) {
        if (studentRepository.existsByStudentCode(studentCode)) {
            throw new IllegalArgumentException("Student code already exists: " + studentCode.getValue());
        }
    }

    public void validateUniqueEmail(String email) {
        if (studentRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
    }

    public StudentCode generateStudentCode() {
        // Simple generation logic - can be improved based on business rules
        String code = "ST" + System.currentTimeMillis();
        return new StudentCode(code.substring(0, Math.min(code.length(), 20)));
    }
}

