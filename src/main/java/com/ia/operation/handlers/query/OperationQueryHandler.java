package com.ia.operation.handlers.query;

import com.ia.operation.handlers.QueryHandler;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Operation;
import com.ia.operation.queries.operation.OperationGetAllQuery;
import com.ia.operation.queries.operation.OperationGetByIdQuery;
import com.ia.operation.queries.operation.OperationGetByYearQuery;

import reactor.core.publisher.Mono;

@Component
public class OperationQueryHandler extends QueryHandler {

    public OperationQueryHandler(QueryGateway gateway) {
        super(gateway);
    }

    public Mono<ServerResponse> operationGet(ServerRequest request) {
        final String operationId = request.pathVariable(OPERATION_ID_KEY);
        return query(() -> OperationGetByIdQuery.builder().operationId(operationId).build(), Operation.class);
    }

    public Mono<ServerResponse> operationGetByear(ServerRequest request) {
        errorReset();
        final String year = request.pathVariable(YEAR_KEY);
        final String userId = request.pathVariable(USER_ID_KEY);
        if (!errors.isEmpty()) {
            return badRequestError(errors);
        }
        return query(() -> OperationGetByYearQuery.builder().year(year).userId(userId).build(), Operation.class);
    }

    public Mono<ServerResponse> operationGetAll(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        return query(() -> OperationGetAllQuery.builder().userId(userId).build(), Operation.class);
    }
}
