package com.ia.operation.handlers.cmd;

import com.ia.operation.commands.creation.UserCreationCmd;
import com.ia.operation.commands.delete.UserDeletionCmd;
import com.ia.operation.commands.update.UserUpdateCmd;
import com.ia.operation.handlers.CommandHandler;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.ObjectIdHelper;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserCmdHandler extends CommandHandler {
    private final AggregateHelper util;
    public UserCmdHandler(CommandGateway gateway, AggregateHelper util) {
        super(gateway);
        this.util = util;
    }

    public Mono<ServerResponse> userAdd(ServerRequest request) {
        final String companyId = request.pathVariable(COMPANY_ID_KEY);
        final Mono<UserCreationCmd> bodyMono = request.bodyToMono(UserCreationCmd.class);
        return bodyMono.map(body->UserCreationCmd.cmdFrom(body)
                .id(ObjectIdHelper.id())
                .companyId(companyId)
                .build()
                .validate(util))
                .flatMap(this::response)
                .switchIfEmpty(ServerResponse.badRequest().body(Mono.just(MISSING_REQUEST_BODY_KEY), String.class));
    }

    public Mono<ServerResponse> userUpdate(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        final Mono<UserUpdateCmd> bodyMono = request.bodyToMono(UserUpdateCmd.class);
        return bodyMono.map(body->UserUpdateCmd.cmdFrom(body)
                .id(userId)
                .build()
                .validate(util))
                .flatMap(this::response)
                .switchIfEmpty(ServerResponse.badRequest().body(Mono.just(MISSING_REQUEST_BODY_KEY), String.class));
    }

    public Mono<ServerResponse> userDelete(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        return response(UserDeletionCmd.builder().id(userId).build().validate(util));
    }
}
