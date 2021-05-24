package com.ia.operation.handlers.cmd;

import com.ia.operation.commands.creation.CompanyCreationCmd;
import com.ia.operation.commands.delete.CompanyDeletionCmd;
import com.ia.operation.commands.update.CompanyUpdateCmd;
import com.ia.operation.handlers.CommandHandler;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.ObjectIdHelper;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RestController
public class CompanyCmdHandler extends CommandHandler {
    private final AggregateHelper util;

    public CompanyCmdHandler(CommandGateway gateway, AggregateHelper util) {
        super(gateway);
        this.util = util;
    }

    public Mono<ServerResponse> companyAdd(ServerRequest request) {
        final Mono<CompanyCreationCmd> bodyMono = request.bodyToMono(CompanyCreationCmd.class);
        return commandComplete(bodyMono.map(body -> CompanyCreationCmd.cmdFrom(body)
                .id(ObjectIdHelper.id())
                .build()
                .validate(util)));
    }

    public Mono<ServerResponse> companyUpdate(ServerRequest request) {
        final String companyId = request.pathVariable(COMPANY_ID_KEY);
        final Mono<CompanyUpdateCmd> bodyMono = request.bodyToMono(CompanyUpdateCmd.class);
        return commandComplete(bodyMono.map(body->CompanyUpdateCmd.cmdFrom(body)
                    .id(companyId)
                    .build()
                    .validate(util)));
    }

    public Mono<ServerResponse>  companyRemove(ServerRequest request) {
        final String companyId = request.pathVariable(COMPANY_ID_KEY);
        return response(CompanyDeletionCmd.builder()
                .id(companyId)
                .build()
                .validate(util));
    }
}
