package com.example.student_management.domain.repository;

import com.example.student_management.domain.model.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    // Có thể bổ sung các phương thức truy vấn tuỳ ý sau này
}

