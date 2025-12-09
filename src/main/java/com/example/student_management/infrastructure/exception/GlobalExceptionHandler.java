package com.example.student_management.infrastructure.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for GraphQL without Spring Web.
 */
@Component
public class GlobalExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof IllegalArgumentException) {
            return buildError(ex.getMessage(), "Bad Request");
        } else if (ex instanceof IllegalStateException) {
            return buildError(ex.getMessage(), "Conflict");
        } else if (ex instanceof ConstraintViolationException cve) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                String fieldPath = violation.getPropertyPath() != null ? violation.getPropertyPath().toString() : "";
                errors.put(fieldPath, violation.getMessage());
            }
            Map<String, Object> extensions = new HashMap<>();
            extensions.put("timestamp", LocalDateTime.now().toString());
            extensions.put("error", "Validation Failed");
            extensions.put("validationErrors", errors);
            return GraphqlErrorBuilder.newError(env)
                    .message("Input validation failed")
                    .extensions(extensions)
                    .build();
        }
        return buildError("An unexpected error occurred", "Internal Server Error");
    }

    private GraphQLError buildError(String message, String errorType) {
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("timestamp", LocalDateTime.now().toString());
        extensions.put("error", errorType);
        return GraphqlErrorBuilder.newError()
                .message(message)
                .extensions(extensions)
                .build();
    }
}
