package com.ia.operation.handlers.query;

import com.ia.operation.handlers.QueryHandler;
import org.apache.commons.lang3.StringUtils;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Projection;
import com.ia.operation.queries.projection.ProjectionByAccountQuery;
import com.ia.operation.queries.projection.ProjectionByYearQuery;

import reactor.core.publisher.Mono;

@Component
public class ProjectionQueryHandler extends QueryHandler {
    public ProjectionQueryHandler(QueryGateway gateway) {
        super(gateway);
    }

    public Mono<ServerResponse> projectionByAccount(ServerRequest request) {
        final String accountId = request.pathVariable(ACCOUNT_ID_KEY);
        final String year = request.pathVariable(YEAR_KEY);
        if (!StringUtils.isNumeric(year)) {
            addError(MISSING_PATH_VARIABLE_PREFIX + YEAR_KEY);
        }
        if (!errors.isEmpty()) {
            return badRequestError(errors);
        }
        return query(() -> ProjectionByAccountQuery.builder().accountId(accountId).year(year).build(), Projection.class);
    }

    public Mono<ServerResponse> projectionByYear(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        final String year = request.pathVariable(YEAR_KEY);
        if (!StringUtils.isNumeric(year)) {
            return badRequestError(MISSING_PATH_VARIABLE_PREFIX + YEAR_KEY);
        }
        return query(() -> ProjectionByYearQuery.builder().year(year).userId(userId).build(), Projection.class);
    }
}
