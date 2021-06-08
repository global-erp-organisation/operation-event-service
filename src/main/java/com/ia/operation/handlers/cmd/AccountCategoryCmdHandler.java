package com.ia.operation.handlers.cmd;

import com.ia.operation.commands.creation.AccountCategoryCreationCmd;
import com.ia.operation.commands.delete.AccountCategoryDeletionCmd;
import com.ia.operation.commands.update.AccountCategoryUpdateCmd;
import com.ia.operation.handlers.CommandHandler;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.ObjectIdHelper;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AccountCategoryCmdHandler extends CommandHandler {

    private final AggregateHelper util;

    public AccountCategoryCmdHandler(CommandGateway gateway, AggregateHelper util) {
        super(gateway);
        this.util = util;
    }

    public Mono<ServerResponse> categoryAdd(ServerRequest request) {
        final Mono<AccountCategoryCreationCmd> bodyMono = request.bodyToMono(AccountCategoryCreationCmd.class);
        return doExecute(bodyMono.map(body -> AccountCategoryCreationCmd.cmdFrom(body).id(ObjectIdHelper.id()).build().validate(util)));
    }

    public Mono<ServerResponse> categoryUpdate(ServerRequest request) {
        final Mono<AccountCategoryUpdateCmd> bodyMono = request.bodyToMono(AccountCategoryUpdateCmd.class);
        final String categoryId = request.pathVariable(CATEGORY_ID_KEY);
        return doExecute(bodyMono.map(body -> AccountCategoryUpdateCmd.cmdFrom(body).id(categoryId).build().validate(util)));
    }

    public Mono<ServerResponse> categoryDelete(ServerRequest request) {
        final String categoryId = request.pathVariable(CATEGORY_ID_KEY);
        return response(AccountCategoryDeletionCmd.builder().id(categoryId).build().validate(util));
    }
}
