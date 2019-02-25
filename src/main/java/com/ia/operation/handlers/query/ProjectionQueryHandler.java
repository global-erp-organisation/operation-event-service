package com.ia.operation.handlers.query;

import org.apache.commons.lang3.StringUtils;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Projection;
import com.ia.operation.handlers.Handler;
import com.ia.operation.queries.projection.ProjectionByAccountQuery;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class ProjectionQueryHandler implements Handler {
    private final QueryGateway gateway;
    
    public Mono<ServerResponse> projectionByAccount(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID);
        if (userId == null) {
            return queryVariableBadRequest(USER_ID);
        }
        final String accountId = request.pathVariable(ACCOUNT_ID);
        if (accountId == null) {
            return queryVariableBadRequest(ACCOUNT_ID);
        }
        final String year = request.pathVariable(YEAR);
        if (year == null || !StringUtils.isNumeric(year)) {
            return queryVariableBadRequest(YEAR);
        }
        return queryComplete(() -> ProjectionByAccountQuery.builder()
                .accountId(accountId)
                .userId(userId)
                .year(year)
                .build(), Projection.class, gateway);
    }
    
    public Mono<ServerResponse> projectionByYear(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID);
        if (userId == null) {
            return queryVariableBadRequest(USER_ID);
        }
        final String year = request.pathVariable(YEAR);
        if (year == null || !StringUtils.isNumeric(year)) {
            return queryVariableBadRequest(YEAR);
        }
        return queryComplete(()->ProjectionByAccountQuery.builder().year(year).userId(userId).build(), Projection.class, gateway);
        
    }
}
