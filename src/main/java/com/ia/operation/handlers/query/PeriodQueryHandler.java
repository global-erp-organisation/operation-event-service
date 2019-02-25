package com.ia.operation.handlers.query;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Period;
import com.ia.operation.handlers.Handler;
import com.ia.operation.queries.period.PeriodGetByIdQuery;
import com.ia.operation.queries.period.PeriodGetByYearQuery;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PeriodQueryHandler implements Handler {
    private QueryGateway gateway;

    public Mono<ServerResponse> periodGetByYear(ServerRequest request) {
        final String year = request.pathVariable(YEAR);
        if (year == null) {
            return queryVariableBadRequest(YEAR);
        }
        return queryComplete(() -> PeriodGetByYearQuery.builder().year(year).build(), Period.class, gateway);
    }

    public Mono<ServerResponse> periodGetById(ServerRequest request) {
        final String periodId = request.pathVariable(PERIOD_ID);
        if (periodId == null) {
            return queryVariableBadRequest(PERIOD_ID);
        }
        return queryComplete(() -> PeriodGetByIdQuery.builder().periodId(periodId).build(), Period.class, gateway);
    }

}
