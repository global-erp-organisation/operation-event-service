package com.ia.operation.handlers.query;

import java.time.LocalDate;
import java.util.Optional;

import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.views.DashboardView;
import com.ia.operation.handlers.Handler;
import com.ia.operation.queries.account.DashboardQuery;
import com.ia.operation.util.history.DashboardBuilder;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DashboardQueryHandler implements Handler {
    private final QueryGateway gateway;
    private final DashboardBuilder builder;

    public Mono<ServerResponse> dashboardGet(ServerRequest request) {
        final String userId = request.pathVariable(USER_ID_KEY);
        final Optional<LocalDate> start = request.queryParam(START_DATE_KEY).map(s -> LocalDate.parse(s));
        final Optional<LocalDate> end = request.queryParam(END_DATE_KEY).map(s -> LocalDate.parse(s));
        final Boolean daily = request.queryParam(DAILY_KEY).map(s->Boolean.valueOf(s)).orElse(false);
        final Boolean monthly = request.queryParam(MONTHLY_KEY).map(s->Boolean.valueOf(s)).orElse(false);
        final Boolean yearly = request.queryParam(YEARLY_KEY).map(s->Boolean.valueOf(s)).orElse(false);
        
        if (userId == null) {
            return badRequestError(MISSING_PATH_VARIABLE_PREFIX + USER_ID_KEY);
        }
        if (!start.isPresent()) {
            return badRequestError(MISSING_QUERY_PARAM_PREFIX + START_DATE_KEY);
        }
        if (!end.isPresent()) {
            return badRequestError(MISSING_QUERY_PARAM_PREFIX + END_DATE_KEY);
        }
        if (!periodCheck(start.get(), end.get())) {
            return badRequestError("Something when wrong with the entered period. The start date should be before the end date.");
        }
        return queryComplete(() -> DashboardQuery.builder()
                .userId(userId)
                .start(start.get())
                .end(end.get())
                .daily(daily)
                .monthly(monthly)
                .yearly(yearly)
                .build(), DashboardView.class, gateway);

    }

    private Boolean periodCheck(LocalDate start, LocalDate end) {
        return (start != null && end != null) && (start.equals(end) || start.isBefore(end));
    }

    @QueryHandler
    public Object dashboardGet(DashboardQuery query) {
        return builder.build(DashboardQuery.from(query));
    }
}
