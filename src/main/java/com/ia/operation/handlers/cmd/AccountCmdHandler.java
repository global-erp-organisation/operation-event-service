package com.ia.operation.handlers.cmd;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.commands.creation.AccountCreationCmd;
import com.ia.operation.commands.delete.AccountDeletionCmd;
import com.ia.operation.commands.update.AccountUpdateCmd;
import com.ia.operation.handlers.Handler;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.ObjectIdUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountCmdHandler implements Handler {
    private final CommandGateway gateway;
    private final AggregateUtil util;

    public Mono<ServerResponse> accountAdd(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID);
        final String categoryId = request.pathVariable(CATEGORY_ID);
        final Mono<AccountCreationCmd> bodyMono = request.bodyToMono(AccountCreationCmd.class);
        return commandComplete(
                bodyMono.map(body -> AccountCreationCmd.from(body)
                        .id(ObjectIdUtil.id())
                        .userId(userId)
                        .categoryId(categoryId)
                        .build().validate(util)),gateway);
    }

    public Mono<ServerResponse> accountUpdate(ServerRequest request) {
        final Mono<AccountUpdateCmd> bodyMono = request.bodyToMono(AccountUpdateCmd.class);
        final String operationId = request.pathVariable(OPERATION_ID);
        return commandComplete(bodyMono.map(body -> AccountUpdateCmd.from(body).id(operationId).build().validate(util)), gateway);
    }

    public Mono<ServerResponse> accountDelete(ServerRequest request) {
        final String operationId = request.pathVariable(OPERATION_ID);
        return response(AccountDeletionCmd.builder().id(operationId).build().validate(util), gateway);
    }

}
