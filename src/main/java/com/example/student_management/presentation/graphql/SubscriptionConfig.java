package com.example.student_management.presentation.graphql;

import com.example.student_management.application.dto.MessageDTO;
import com.example.student_management.application.dto.UserDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SubscriptionConfig {
    @Bean
    public Sinks.Many<UserDTO> userRegisteredSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    public Sinks.Many<MessageDTO> messageSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
