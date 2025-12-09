package com.example.student_management;

import com.example.student_management.application.dto.CreateStudentRequest;
import com.example.student_management.application.dto.StudentDTO;
import com.example.student_management.application.service.StudentApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StudentManagementApplicationTests {

	@Autowired
	private StudentApplicationService studentApplicationService;

	@Test
	void contextLoads() {
		assertThat(studentApplicationService).isNotNull();
	}

	@Test
	void testCreateStudent() {
		CreateStudentRequest request = CreateStudentRequest.builder()
				.studentCode("ST123456")
				.firstName("Nguyen")
				.lastName("Van A")
				.email("test@example.com")
				.dateOfBirth(LocalDate.of(2000, 1, 1))
				.street("123 Main St")
				.city("Ho Chi Minh")
				.state("HCM")
				.postalCode("700000")
				.country("Vietnam")
				.build();

		StudentDTO student = studentApplicationService.createStudent(request);

		assertThat(student).isNotNull();
		assertThat(student.getStudentCode()).isEqualTo("ST123456");
		assertThat(student.getFirstName()).isEqualTo("Nguyen");
		assertThat(student.getLastName()).isEqualTo("Van A");
	}
}

