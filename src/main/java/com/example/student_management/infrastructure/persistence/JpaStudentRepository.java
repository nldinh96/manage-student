package com.example.student_management.infrastructure.persistence;

import com.example.student_management.domain.model.student.Student;
import com.example.student_management.domain.model.student.StudentCode;
import com.example.student_management.domain.model.student.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository Interface
 */
@Repository
public interface JpaStudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.studentCode.value = :code")
    Optional<Student> findByStudentCode(@Param("code") String code);

    List<Student> findByStatus(StudentStatus status);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.studentCode.value = :code")
    boolean existsByStudentCode(@Param("code") String code);

    boolean existsByEmail(String email);
}

