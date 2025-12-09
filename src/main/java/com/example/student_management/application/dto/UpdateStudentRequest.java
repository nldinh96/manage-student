package com.example.student_management.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for updating student information - all fields are optional to allow partial updates
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequest {

    // optional: no @NotBlank so null means "don't update"
    private String firstName;

    // optional
    private String lastName;

    // optional; @Email allows null
    @Email(message = "Email must be valid")
    private String email;

    // optional; @Past allows null
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
