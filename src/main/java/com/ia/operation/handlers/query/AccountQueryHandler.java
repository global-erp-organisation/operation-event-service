package com.ia.operation.handlers.query;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Account;
import com.ia.operation.handlers.Handler;
import com.ia.operation.queries.account.AccountGetAllQuery;
import com.ia.operation.queries.account.AccountGetByIdQuery;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountQueryHandler implements Handler {
    private final QueryGateway gateway;

    public Mono<ServerResponse> accountGetAll(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        if (userId == null) {
            return badRequestError(USER_ID_KEY);
        }
        return queryComplete(() -> AccountGetAllQuery.builder().userId(userId).build(), Account.class, gateway);
    }

    public Mono<ServerResponse> accountGet(ServerRequest request) {
        final String accountId = request.pathVariable(ACCOUNT_ID_KEY);
        if (accountId == null) {
            return badRequestError(ACCOUNT_ID_KEY);
        }
        return queryComplete(() -> AccountGetByIdQuery.builder().accountId(accountId).build(), Account.class, gateway);
    }
}
