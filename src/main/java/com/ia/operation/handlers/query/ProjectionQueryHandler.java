package com.ia.operation.handlers.query;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Projection;
import com.ia.operation.handlers.Handler;
import com.ia.operation.queries.projection.ProjectionByAccountQuery;
import com.ia.operation.queries.projection.ProjectionByYearQuery;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class ProjectionQueryHandler implements Handler {
    private final QueryGateway gateway;

    public Mono<ServerResponse> projectionByAccount(ServerRequest request) {
        final String accountId = request.pathVariable(ACCOUNT_ID_KEY);
        final List<String> errors = new ArrayList<>();
        if (accountId == null) {
            errorItemAdd(errors, MISSING_PATH_VARIABLE_PREFIX + ACCOUNT_ID_KEY);
        }
        final String year = request.pathVariable(YEAR_KEY);
        if (year == null || !StringUtils.isNumeric(year)) {
            errorItemAdd(errors, MISSING_PATH_VARIABLE_PREFIX + YEAR_KEY);
        }
        if (!errors.isEmpty()) {
            return badRequestError(errors);
        }
        return queryComplete(() -> ProjectionByAccountQuery.builder().accountId(accountId).year(year).build(), Projection.class, gateway);
    }

    public Mono<ServerResponse> projectionByYear(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        if (userId == null) {
            return badRequestError(MISSING_PATH_VARIABLE_PREFIX + USER_ID_KEY);
        }
        final String year = request.pathVariable(YEAR_KEY);
        if (year == null || !StringUtils.isNumeric(year)) {
            return badRequestError(MISSING_PATH_VARIABLE_PREFIX + YEAR_KEY);
        }
        return queryComplete(() -> ProjectionByYearQuery.builder().year(year).userId(userId).build(), Projection.class, gateway);

    }
}
