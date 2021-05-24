package com.ia.operation.handlers.query;

import com.ia.operation.handlers.QueryHandler;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Account;
import com.ia.operation.queries.account.AccountGetAllQuery;
import com.ia.operation.queries.account.AccountGetByIdQuery;

import reactor.core.publisher.Mono;

@Component
public class AccountQueryHandler extends QueryHandler {
    public AccountQueryHandler(QueryGateway gateway) {
        super(gateway);
    }

    public Mono<ServerResponse> accountGetAll(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        return query(() -> AccountGetAllQuery.builder().userId(userId).build(), Account.class);
    }

    public Mono<ServerResponse> accountGet(ServerRequest request) {
        final String accountId = request.pathVariable(ACCOUNT_ID_KEY);
        return query(() -> AccountGetByIdQuery.builder().accountId(accountId).build(), Account.class);
    }
}
