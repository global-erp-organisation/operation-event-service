package com.ia.operation.handlers.cmd;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.commands.creation.CompanyCreationCmd;
import com.ia.operation.commands.delete.CompanyDeletionCmd;
import com.ia.operation.commands.update.CompanyUpdateCmd;
import com.ia.operation.handlers.Handler;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.ObjectIdUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CompanyCmdHandler implements Handler {
    private final CommandGateway gateway;
    private final AggregateUtil util;
    
    public Mono<ServerResponse> companyAdd(ServerRequest request) {
        final Mono<CompanyCreationCmd> bodyMono = request.bodyToMono(CompanyCreationCmd.class);
        return commandComplete(bodyMono.map(body -> CompanyCreationCmd.cmdFrom(body)
                .id(ObjectIdUtil.id())
                .build()
                .validate(util)), gateway);
    }

    public Mono<ServerResponse> companyUpdate(ServerRequest request) {
        final String companyId = request.pathVariable(COMPANY_ID_KEY);
        final Mono<CompanyUpdateCmd> bodyMono = request.bodyToMono(CompanyUpdateCmd.class);
        return commandComplete(bodyMono.map(body->CompanyUpdateCmd.cmdFrom(body)
                    .id(companyId)
                    .build()
                    .validate(util)),gateway);
    }

    public Mono<ServerResponse>  companyRemove(ServerRequest request) {
        final String companyId = request.pathVariable(COMPANY_ID_KEY);
        return response(CompanyDeletionCmd.builder()
                .id(companyId)
                .build()
                .validate(util), gateway);
    }
}
