package com.ia.operation.handlers.cmd;

import java.time.LocalDate;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.commands.creation.OperationCreationCmd;
import com.ia.operation.commands.creation.OperationTransfertCmd;
import com.ia.operation.commands.delete.OperationDeletionCmd;
import com.ia.operation.commands.update.OperationUpdateCmd;
import com.ia.operation.enums.OperationType;
import com.ia.operation.handlers.Handler;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.ObjectIdHelper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class OperationCmdHandler implements Handler {

    private final CommandGateway gateway;
    private final AggregateHelper util;

    public Mono<ServerResponse> operationAdd(ServerRequest request) {
        final String accountId = request.pathVariable(ACCOUNT_ID_KEY);
        return execute(request.bodyToMono(OperationCreationCmd.class), accountId);
    }
    
    public Mono<ServerResponse> operationTransfert(ServerRequest request) {
        return request.bodyToMono(OperationTransfertCmd.class).flatMap(body -> {
            final LocalDate date = body.getOperationDate() == null ? LocalDate.now() : body.getOperationDate();
            final OperationCreationCmd source = OperationCreationCmd.builder()
                    .id(ObjectIdHelper.id())
                    .accountId(body.getSourceAccountId())
                    .amount(body.getAmount())
                    .description(body.getDescription())
                    .operationDate(date)
                    .operationType(OperationType.EXPENSE)
                    .build();
            execute(Mono.just(source), body.getSourceAccountId());
            final OperationCreationCmd target = OperationCreationCmd.builder()
                    .id(ObjectIdHelper.id())
                    .accountId(body.getTargetAccountId())
                    .amount(body.getAmount())
                    .description(body.getDescription())
                    .operationDate(date)
                    .operationType(OperationType.REVENUE)
                    .build();
           return execute(Mono.just(target), body.getTargetAccountId()); 
        });
    }

    private Mono<ServerResponse> execute(Mono<OperationCreationCmd> cmd, String accountId) {
        return commandComplete(cmd.map(body -> {
            final LocalDate date = body.getOperationDate() == null ? LocalDate.now() : body.getOperationDate();
            return OperationCreationCmd.cmdFrom(body).id(ObjectIdHelper.id()).operationDate(date).accountId(accountId).build().validate(util);
        }), gateway);

    }

    public Mono<ServerResponse> operationUpdate(ServerRequest request) {
        final String operationId = request.pathVariable(OPERATION_ID_KEY);
        return commandComplete(request.bodyToMono(OperationUpdateCmd.class).map(body -> {
            return OperationUpdateCmd.cmdFrom(body).id(operationId).build().validate(util);
        }), gateway);
    }

    public Mono<ServerResponse> operationDelete(ServerRequest request) {
        final String operationId = request.pathVariable(OPERATION_ID_KEY);
        return response(OperationDeletionCmd.builder().id(operationId).build().validate(util), gateway);
    }
}
