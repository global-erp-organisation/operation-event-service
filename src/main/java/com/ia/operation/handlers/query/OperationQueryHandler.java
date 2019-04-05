package com.ia.operation.handlers.query;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Operation;
import com.ia.operation.handlers.Handler;
import com.ia.operation.queries.operation.OperationGetAllQuery;
import com.ia.operation.queries.operation.OperationGetByIdQuery;
import com.ia.operation.queries.operation.OperationGetByYearQuery;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OperationQueryHandler implements Handler {
    private QueryGateway gateway;

    public Mono<ServerResponse> operationGet(ServerRequest request) {
        final String operationId = request.pathVariable(OPERATION_ID_KEY);
        if (operationId == null) {
            return badRequestError(MISSING_PATH_VARIABLE_PREFIX + OPERATION_ID_KEY);
        }
        return queryComplete(() -> OperationGetByIdQuery.builder().operationId(operationId).build(), Operation.class, gateway);
    }

    public Mono<ServerResponse> operationGetByear(ServerRequest request) {
        final String year = request.pathVariable(YEAR_KEY);
        final List<String> errors = new ArrayList<>();
        if (year == null) {
            errorItemAdd(errors, MISSING_PATH_VARIABLE_PREFIX + YEAR_KEY);
        }
        final String userId = request.pathVariable(USER_ID_KEY);
        if (userId == null) {
            errorItemAdd(errors, MISSING_PATH_VARIABLE_PREFIX + USER_ID_KEY);
        }
        if (!errors.isEmpty()) {
            return badRequestError(errors);
        }
        return queryComplete(() -> OperationGetByYearQuery.builder().year(year).userId(userId).build(), Operation.class, gateway);
    }

    public Mono<ServerResponse> operationGetAll(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        if (userId == null) {
            return badRequestError(MISSING_PATH_VARIABLE_PREFIX + USER_ID_KEY);
        }
        return queryComplete(() -> OperationGetAllQuery.builder().userId(userId).build(), Operation.class, gateway);
    }
}
