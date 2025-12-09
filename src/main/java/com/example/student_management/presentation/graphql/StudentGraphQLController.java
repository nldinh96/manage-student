package com.example.student_management.presentation.graphql;

import com.example.student_management.application.dto.CreateStudentRequest;
import com.example.student_management.application.dto.StudentDTO;
import com.example.student_management.application.dto.UpdateStudentRequest;
import com.example.student_management.application.service.StudentApplicationService;
import com.example.student_management.domain.model.student.StudentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * GraphQL Controller for Student operations
 */
@Controller
@RequiredArgsConstructor
public class StudentGraphQLController {

    private final StudentApplicationService studentApplicationService;

    @QueryMapping
    public StudentDTO student(@Argument Long id) {
        return studentApplicationService.getStudentById(id);
    }

    @QueryMapping
    public StudentDTO studentByCode(@Argument String code) {
        return studentApplicationService.getStudentByCode(code);
    }

    @QueryMapping
    public List<StudentDTO> students(@Argument StudentStatus status) {
        return status != null
                ? studentApplicationService.getStudentsByStatus(status)
                : studentApplicationService.getAllStudents();
    }

    @MutationMapping
    public StudentDTO createStudent(@Argument CreateStudentRequest input) {
        return studentApplicationService.createStudent(input);
    }

    @MutationMapping
    public StudentDTO updateStudent(@Argument Long id, @Argument UpdateStudentRequest input) {
        return studentApplicationService.updateStudent(id, input);
    }

    @MutationMapping
    public Boolean activateStudent(@Argument Long id) {
        studentApplicationService.activateStudent(id);
        return true;
    }

    @MutationMapping
    public Boolean suspendStudent(@Argument Long id) {
        studentApplicationService.suspendStudent(id);
        return true;
    }

    @MutationMapping
    public Boolean graduateStudent(@Argument Long id) {
        studentApplicationService.graduateStudent(id);
        return true;
    }

    @MutationMapping
    public Boolean deleteStudent(@Argument Long id) {
        studentApplicationService.deleteStudent(id);
        return true;
    }
}

