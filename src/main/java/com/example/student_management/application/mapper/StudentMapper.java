package com.example.student_management.application.mapper;

import com.example.student_management.application.dto.StudentDTO;
import com.example.student_management.domain.model.student.Student;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Student entity and DTOs
 */
@Component
public class StudentMapper {

    public StudentDTO toDTO(Student student) {
        if (student == null) {
            return null;
        }

        return StudentDTO.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode().getValue())
                .firstName(student.getFullName().getFirstName())
                .lastName(student.getFullName().getLastName())
                .email(student.getEmail())
                .dateOfBirth(student.getDateOfBirth())
                .street(student.getAddress() != null ? student.getAddress().getStreet() : null)
                .city(student.getAddress() != null ? student.getAddress().getCity() : null)
                .state(student.getAddress() != null ? student.getAddress().getState() : null)
                .postalCode(student.getAddress() != null ? student.getAddress().getPostalCode() : null)
                .country(student.getAddress() != null ? student.getAddress().getCountry() : null)
                .status(student.getStatus())
                .enrollmentDate(student.getEnrollmentDate())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
