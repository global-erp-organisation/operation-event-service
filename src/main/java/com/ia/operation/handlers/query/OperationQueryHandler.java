package com.ia.operation.handlers.query;

import com.ia.operation.handlers.QueryHandler;
import com.ia.operation.queries.operation.OperationGetByPeriodQuery;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Operation;
import com.ia.operation.queries.operation.OperationGetAllQuery;
import com.ia.operation.queries.operation.OperationGetByIdQuery;
import com.ia.operation.queries.operation.OperationGetByYearQuery;

import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class OperationQueryHandler extends QueryHandler {

    public OperationQueryHandler(QueryGateway gateway) {
        super(gateway);
    }

    public Mono<ServerResponse> operationGet(ServerRequest request) {
        final String operationId = request.pathVariable(OPERATION_ID_KEY);
        return query(() -> OperationGetByIdQuery.builder().operationId(operationId).build(), Operation.class);
    }

    public Mono<ServerResponse> operationsByYear(ServerRequest request) {
        errorReset();
        final String year = request.queryParam(YEAR_KEY).orElseGet(() -> {
            addError(String.format("%s query param is mandatory.", YEAR_KEY));
            return null;
        });
        final String userId = request.pathVariable(USER_ID_KEY);
        if (!errors.isEmpty()) {
            return badRequestError(errors);
        }
        return queryList(() -> OperationGetByYearQuery.builder().year(year).userId(userId).build(), Operation.class);
    }

    public Mono<ServerResponse> operationsByMonth(ServerRequest request) {
        errorReset();
        final String periodId = request.pathVariable(PERIOD_ID_KEY);
        final String userId = request.pathVariable(USER_ID_KEY);
        if (!errors.isEmpty()) {
            return badRequestError(errors);
        }
        return queryList(() -> OperationGetByPeriodQuery.builder().periodId(periodId).userId(userId).build(), Operation.class);
    }

    public Mono<ServerResponse> operationGetAll(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        return queryList(() -> OperationGetAllQuery.builder().userId(userId).build(), Operation.class);
    }
}
