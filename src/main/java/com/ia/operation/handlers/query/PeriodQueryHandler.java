package com.ia.operation.handlers.query;

import com.ia.operation.handlers.QueryHandler;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Period;
import com.ia.operation.queries.period.PeriodGetByIdQuery;
import com.ia.operation.queries.period.PeriodGetByYearQuery;

import reactor.core.publisher.Mono;

@Component
public class PeriodQueryHandler extends QueryHandler {
    public PeriodQueryHandler(QueryGateway gateway) {
        super(gateway);
    }

    public Mono<ServerResponse> periodGetByYear(ServerRequest request) {
        final String year = request.pathVariable(YEAR_KEY);
        return query(() -> PeriodGetByYearQuery.builder().year(year).build(), Period.class);
    }

    public Mono<ServerResponse> periodGetById(ServerRequest request) {
        final String periodId = request.pathVariable(PERIOD_ID_KEY);
        return query(() -> PeriodGetByIdQuery.builder().periodId(periodId).build(), Period.class);
    }

}
