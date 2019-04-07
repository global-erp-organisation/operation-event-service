package com.ia.operation.handlers.cmd;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.commands.creation.AccountCreationCmd;
import com.ia.operation.commands.delete.AccountDeletionCmd;
import com.ia.operation.commands.update.AccountUpdateCmd;
import com.ia.operation.handlers.Handler;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.ObjectIdHelper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountCmdHandler implements Handler {
    private final CommandGateway gateway;
    private final AggregateHelper util;

    public Mono<ServerResponse> accountAdd(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        final String categoryId = request.pathVariable(CATEGORY_ID_KEY);
        final Mono<AccountCreationCmd> bodyMono = request.bodyToMono(AccountCreationCmd.class);
        return commandComplete(
                bodyMono.map(body -> AccountCreationCmd.cmdFrom(body)
                        .id(ObjectIdHelper.id())
                        .userId(userId)
                        .categoryId(categoryId)
                        .build().validate(util)),gateway);
    }

    public Mono<ServerResponse> accountUpdate(ServerRequest request) {
        final Mono<AccountUpdateCmd> bodyMono = request.bodyToMono(AccountUpdateCmd.class);
        final String accountId = request.pathVariable(ACCOUNT_ID_KEY);
        return commandComplete(bodyMono.map(body -> AccountUpdateCmd.cmdFrom(body).id(accountId).build().validate(util)), gateway);
    }

    public Mono<ServerResponse> accountDelete(ServerRequest request) {
        final String operationId = request.pathVariable(OPERATION_ID_KEY);
        return response(AccountDeletionCmd.builder().id(operationId).build().validate(util), gateway);
    }

}
