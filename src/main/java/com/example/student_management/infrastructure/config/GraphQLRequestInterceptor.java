package com.example.student_management.infrastructure.config;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Interceptor to pass HTTP headers to GraphQL context
 */
@Component
public class GraphQLRequestInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String authHeader = request.getHeaders().getFirst("Authorization");

        // Only put Authorization header if it's not null
        if (authHeader != null) {
            request.configureExecutionInput((executionInput, builder) ->
                    builder.graphQLContext(ctx -> ctx.put("Authorization", authHeader)).build()
            );
        }

        return chain.next(request);
    }
}
