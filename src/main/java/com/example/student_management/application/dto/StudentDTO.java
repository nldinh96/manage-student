package com.example.student_management.application.dto;

import com.example.student_management.domain.model.student.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Student
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String studentCode;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private StudentStatus status;
    private LocalDate enrollmentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
