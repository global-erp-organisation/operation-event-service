package com.ia.operation.handlers.query;

import com.ia.operation.handlers.QueryHandler;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.User;
import com.ia.operation.queries.user.UserGetByEmailQuery;
import com.ia.operation.queries.user.UserGetByIdQuery;

import reactor.core.publisher.Mono;

@Component
public class UserQueryHandler extends QueryHandler {

    public UserQueryHandler(QueryGateway gateway) {
        super(gateway);
    }

    public Mono<ServerResponse> userGet(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        return query(() -> UserGetByIdQuery.builder().userId(userId).build(), User.class);
    }

    public Mono<ServerResponse> userGetByEmail(ServerRequest request) {
        return request.queryParam(EMAIL_KEY)
                .map(e -> query(() -> UserGetByEmailQuery.builder().email(e).build(), User.class))
                .orElseGet(() -> badRequestError(MISSING_QUERY_PARAM_PREFIX + EMAIL_KEY));
    }
}
