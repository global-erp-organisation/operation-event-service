package com.ia.operation.handlers.cmd;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.commands.creation.AccountCategoryCreationCmd;
import com.ia.operation.commands.delete.AccountCategoryDeletionCmd;
import com.ia.operation.commands.update.AccountCategoryUpdateCmd;
import com.ia.operation.handlers.Handler;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.ObjectIdUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountCategoryCmdHandler implements Handler {

    private final CommandGateway gateway;
    private final AggregateUtil util;

    public Mono<ServerResponse> categoryAdd(ServerRequest request) {
        final Mono<AccountCategoryCreationCmd> bodyMono = request.bodyToMono(AccountCategoryCreationCmd.class);
        return commandComplete(bodyMono.map(body -> AccountCategoryCreationCmd.cmdFrom(body).id(ObjectIdUtil.id()).build().validate(util)), gateway);
    }

    public Mono<ServerResponse> categoryUpdate(ServerRequest request) {
        final Mono<AccountCategoryUpdateCmd> bodyMono = request.bodyToMono(AccountCategoryUpdateCmd.class);
        final String categoryId = request.pathVariable(CATEGORY_ID);
        return commandComplete(bodyMono.map(body -> AccountCategoryUpdateCmd.cmdFrom(body).id(categoryId).build().validate(util)), gateway);
    }

    public Mono<ServerResponse> categoryDelete(ServerRequest request) {
        final String categoryId = request.pathVariable(CATEGORY_ID);
        return response(AccountCategoryDeletionCmd.builder().categoryId(categoryId).build().validate(util), gateway);
    }
}
