package com.example.student_management.infrastructure.config;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Custom exception resolver for GraphQL to handle security exceptions
 */
@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof AccessDeniedException) {
            return GraphqlErrorBuilder.newError(env)
                    .message("Access Denied: You don't have permission to access this resource")
                    .errorType(ErrorType.FORBIDDEN)
                    .build();
        }

        if (ex instanceof AuthenticationCredentialsNotFoundException) {
            return GraphqlErrorBuilder.newError(env)
                    .message("Unauthorized: Please login first")
                    .errorType(ErrorType.UNAUTHORIZED)
                    .build();
        }

        // For other exceptions, return generic error
        return GraphqlErrorBuilder.newError(env)
                .message(ex.getMessage())
                .errorType(ErrorType.INTERNAL_ERROR)
                .build();
    }
}

