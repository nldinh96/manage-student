package com.example.student_management.presentation.graphql;

import com.example.student_management.application.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Controller
@RequiredArgsConstructor
public class UserSubscriptionController {
    private final Sinks.Many<UserDTO> userRegisteredSink;

    @SubscriptionMapping
    public Publisher<UserDTO> newUserRegistered() {
        return userRegisteredSink.asFlux();
    }
}

