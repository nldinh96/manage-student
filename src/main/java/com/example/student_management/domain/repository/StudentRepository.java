package com.example.student_management.domain.repository;

import com.example.student_management.domain.model.student.Student;
import com.example.student_management.domain.model.student.StudentCode;
import com.example.student_management.domain.model.student.StudentStatus;

import java.util.List;
import java.util.Optional;

/**
 * Domain Repository Interface for Student Aggregate
 */
public interface StudentRepository {

    Student save(Student student);

    Optional<Student> findById(Long id);

    Optional<Student> findByStudentCode(StudentCode studentCode);

    List<Student> findAll();

    List<Student> findByStatus(StudentStatus status);

    boolean existsByStudentCode(StudentCode studentCode);

    boolean existsByEmail(String email);

    void deleteById(Long id);

    long count();
}

