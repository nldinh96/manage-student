package com.example.student_management.presentation.graphql;

import com.example.student_management.application.dto.MessageDTO;
import com.example.student_management.application.service.MessageService;
import com.example.student_management.domain.model.user.User;
import com.example.student_management.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MessageGraphQLController {
    private final MessageService messageService;
    private final UserRepository userRepository;

    @MutationMapping
    public MessageDTO sendMessage(@Argument SendMessageInput input) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Not authenticated");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        UUID senderId = user.getId();
        UUID receiverId = UUID.fromString(input.getReceiverId());
        return messageService.sendMessage(senderId, receiverId, input.getContent());
    }

    public static class SendMessageInput {
        private String receiverId;
        private String content;

        public String getReceiverId() { return receiverId; }
        public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
