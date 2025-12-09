package com.example.student_management.application.service;

import com.example.student_management.application.dto.CreateStudentRequest;
import com.example.student_management.application.dto.StudentDTO;
import com.example.student_management.application.dto.UpdateStudentRequest;
import com.example.student_management.application.mapper.StudentMapper;
import com.example.student_management.domain.model.student.*;
import com.example.student_management.domain.repository.StudentRepository;
import com.example.student_management.domain.service.StudentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application Service for Student use cases
 */
@Service
@RequiredArgsConstructor
@Transactional
public class StudentApplicationService {

    private final StudentRepository studentRepository;
    private final StudentDomainService studentDomainService;
    private final StudentMapper studentMapper;

    public StudentDTO createStudent(CreateStudentRequest request) {
        // Validate uniqueness
        StudentCode studentCode = new StudentCode(request.getStudentCode());
        studentDomainService.validateUniqueStudentCode(studentCode);
        studentDomainService.validateUniqueEmail(request.getEmail());

        // Create value objects
        FullName fullName = new FullName(request.getFirstName(), request.getLastName());
        Address address = new Address(
                request.getStreet(),
                request.getCity(),
                request.getState(),
                request.getPostalCode(),
                request.getCountry()
        );

        // Create aggregate
        Student student = new Student(
                studentCode,
                fullName,
                request.getEmail(),
                request.getDateOfBirth(),
                address
        );

        // Save and return
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    public StudentDTO updateStudent(Long id, UpdateStudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        // Update personal info
        FullName fullName = new FullName(request.getFirstName(), request.getLastName());
        student.updatePersonalInfo(fullName, request.getEmail(), request.getDateOfBirth());

        // Update address
        Address address = new Address(
                request.getStreet(),
                request.getCity(),
                request.getState(),
                request.getPostalCode(),
                request.getCountry()
        );
        student.updateAddress(address);

        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        return studentMapper.toDTO(student);
    }

    @Transactional(readOnly = true)
    public StudentDTO getStudentByCode(String code) {
        StudentCode studentCode = new StudentCode(code);
        Student student = studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with code: " + code));
        return studentMapper.toDTO(student);
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByStatus(StudentStatus status) {
        return studentRepository.findByStatus(status).stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void activateStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        student.activate();
        studentRepository.save(student);
    }

    public void suspendStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        student.suspend();
        studentRepository.save(student);
    }

    public void graduateStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        student.graduate();
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}

