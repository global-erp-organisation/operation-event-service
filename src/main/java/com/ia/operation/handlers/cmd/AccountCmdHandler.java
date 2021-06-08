package com.ia.operation.handlers.cmd;

import com.ia.operation.commands.creation.AccountCreationCmd;
import com.ia.operation.commands.delete.AccountDeletionCmd;
import com.ia.operation.commands.update.AccountUpdateCmd;
import com.ia.operation.handlers.CommandHandler;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.ObjectIdHelper;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AccountCmdHandler extends CommandHandler {
    private final AggregateHelper util;

    public AccountCmdHandler(CommandGateway gateway, AggregateHelper util) {
        super(gateway);
        this.util = util;
    }

    public Mono<ServerResponse> accountAdd(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        final String categoryId = request.pathVariable(CATEGORY_ID_KEY);
        final Mono<AccountCreationCmd> bodyMono = request.bodyToMono(AccountCreationCmd.class);
        return doExecute(
                bodyMono.map(body -> AccountCreationCmd.cmdFrom(body)
                        .id(ObjectIdHelper.id())
                        .userId(userId)
                        .categoryId(categoryId)
                        .build().validate(util)));
    }
    public Mono<ServerResponse> accountUpdate(ServerRequest request) {
        final Mono<AccountUpdateCmd> bodyMono = request.bodyToMono(AccountUpdateCmd.class);
        final String accountId = request.pathVariable(ACCOUNT_ID_KEY);
        return doExecute(bodyMono.map(body -> AccountUpdateCmd.cmdFrom(body).id(accountId).build().validate(util)));
    }

    public Mono<ServerResponse> accountDelete(ServerRequest request) {
        final String operationId = request.pathVariable(OPERATION_ID_KEY);
        return response(AccountDeletionCmd.builder().id(operationId).build().validate(util));
    }
}
