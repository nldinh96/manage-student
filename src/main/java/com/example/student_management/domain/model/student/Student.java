package com.example.student_management.domain.model.student;

import com.example.student_management.domain.model.shared.AggregateRoot;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Student Aggregate Root
 */
@Entity
@Table(name = "students")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends AggregateRoot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private StudentCode studentCode;

    @Embedded
    private FullName fullName;

    @Column(nullable = false)
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentStatus status;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor for creating new student
    public Student(StudentCode studentCode, FullName fullName, String email,
                   LocalDate dateOfBirth, Address address) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.status = StudentStatus.ACTIVE;
        this.enrollmentDate = LocalDate.now();
        this.createdAt = LocalDateTime.now();
    }

    // Business methods
    public void updatePersonalInfo(FullName fullName, String email, LocalDate dateOfBirth) {
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateAddress(Address address) {
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status == StudentStatus.GRADUATED) {
            throw new IllegalStateException("Cannot activate a graduated student");
        }
        this.status = StudentStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void suspend() {
        if (this.status == StudentStatus.GRADUATED) {
            throw new IllegalStateException("Cannot suspend a graduated student");
        }
        this.status = StudentStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void graduate() {
        if (this.status != StudentStatus.ACTIVE) {
            throw new IllegalStateException("Only active students can graduate");
        }
        this.status = StudentStatus.GRADUATED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status == StudentStatus.ACTIVE;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
