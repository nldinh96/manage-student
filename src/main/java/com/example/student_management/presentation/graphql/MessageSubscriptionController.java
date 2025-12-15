package com.example.student_management.presentation.graphql;

import com.example.student_management.application.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MessageSubscriptionController {
    private final Sinks.Many<MessageDTO> messageSink;

    @SubscriptionMapping
    public Publisher<MessageDTO> newMessageReceived(@Argument String userId) {
        UUID userUUID = UUID.fromString(userId);
        return messageSink.asFlux().filter(msg -> msg.getReceiverId().equals(userUUID));
    }
}

