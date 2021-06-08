package com.ia.operation.handlers.query;

import com.ia.operation.handlers.CmdResponse;
import com.ia.operation.handlers.QueryHandler;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Period;
import com.ia.operation.queries.period.PeriodGetByIdQuery;
import com.ia.operation.queries.period.PeriodGetByYearQuery;

import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class PeriodQueryHandler extends QueryHandler {
    public PeriodQueryHandler(QueryGateway gateway) {
        super(gateway);
    }

    public Mono<ServerResponse> periodGetByYear(ServerRequest request) {
        final Optional<String> year = request.queryParam(YEAR_KEY);
        return year.map(y -> queryList(() -> PeriodGetByYearQuery.builder().year(y).build(), Period.class))
                .orElse(badRequestComplete(() -> CmdResponse.<String, String>builder().body(String.format("%s%s", MISSING_QUERY_PARAM_PREFIX, YEAR_KEY)).build(), CmdResponse.class));

    }

    public Mono<ServerResponse> periodGetById(ServerRequest request) {
        final String periodId = request.pathVariable(PERIOD_ID_KEY);
        return query(() -> PeriodGetByIdQuery.builder().periodId(periodId).build(), Period.class);
    }

}
