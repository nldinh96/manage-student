package com.example.student_management.application.service;

import com.example.student_management.application.dto.MessageDTO;
import com.example.student_management.domain.model.message.Message;
import com.example.student_management.domain.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final Sinks.Many<MessageDTO> messageSink;

    @Transactional
    public MessageDTO sendMessage(UUID senderId, UUID receiverId, String content) {
        Message message = Message.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .timestamp(Instant.now())
                .build();
        Message saved = messageRepository.save(message);
        MessageDTO dto = MessageDTO.builder()
                .id(saved.getId())
                .senderId(saved.getSenderId())
                .receiverId(saved.getReceiverId())
                .content(saved.getContent())
                .timestamp(saved.getTimestamp())
                .build();
        messageSink.tryEmitNext(dto);
        return dto;
    }
}

