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
        final String userId = request.pathVariable(USER_ID);
        if (userId == null) {
            return queryVariableBadRequest(USER_ID);
        }
        return queryComplete(() -> AccountGetAllQuery.builder().userId(userId).build(), Account.class, gateway);
    }

    public Mono<ServerResponse> accountGet(ServerRequest request) {
        final String accountId = request.pathVariable(ACCOUNT_ID);
        if (accountId == null) {
            return queryVariableBadRequest(ACCOUNT_ID);
        }
        return queryComplete(() -> AccountGetByIdQuery.builder().accountId(accountId).build(), Account.class, gateway);
    }
}
