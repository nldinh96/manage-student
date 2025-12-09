package com.example.student_management.infrastructure.persistence;

import com.example.student_management.domain.model.student.Student;
import com.example.student_management.domain.model.student.StudentCode;
import com.example.student_management.domain.model.student.StudentStatus;
import com.example.student_management.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of Domain Repository using Spring Data JPA
 */
@Component
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {

    private final JpaStudentRepository jpaRepository;

    @Override
    public Student save(Student student) {
        return jpaRepository.save(student);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Student> findByStudentCode(StudentCode studentCode) {
        return jpaRepository.findByStudentCode(studentCode.getValue());
    }

    @Override
    public List<Student> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Student> findByStatus(StudentStatus status) {
        return jpaRepository.findByStatus(status);
    }

    @Override
    public boolean existsByStudentCode(StudentCode studentCode) {
        return jpaRepository.existsByStudentCode(studentCode.getValue());
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}

