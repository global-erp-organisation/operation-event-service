package com.ia.operation.handlers.query;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.User;
import com.ia.operation.handlers.Handler;
import com.ia.operation.queries.user.UserGetByIdQuery;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserQueryHandler implements Handler {
    private final QueryGateway gateway;

    public Mono<ServerResponse> userGet(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID);
        if (userId == null) {
            return queryVariableBadRequest(USER_ID);
        }
        return queryComplete(() -> UserGetByIdQuery.builder().userId(userId).build(), User.class, gateway);
    }
}
