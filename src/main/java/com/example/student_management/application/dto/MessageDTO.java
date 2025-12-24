package com.example.student_management.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private UUID id;
    public UUID senderId;
    private UUID receiverId;
    private String content;
    public Instant timestamp;
    
    public UUID getId() { return id; }
    public UUID getReceiverId() { return receiverId; }
    public String getContent() { return content; }
}

