package com.ia.operation.handlers.query;

import java.time.LocalDate;
import java.util.Optional;

import com.ia.operation.handlers.QueryHandler;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.views.DashboardView;
import com.ia.operation.helper.history.DashboardBuilder;
import com.ia.operation.queries.account.DashboardQuery;

import reactor.core.publisher.Mono;

@Component
public class DashboardQueryHandler extends QueryHandler {

    private final DashboardBuilder builder;
    public DashboardQueryHandler(QueryGateway gateway, DashboardBuilder builder) {
        super(gateway);
        this.builder = builder;
    }


    public Mono<ServerResponse> dashboardGet(ServerRequest request) {
        errorReset();
        final String userId = request.pathVariable(USER_ID_KEY);
        final Optional<LocalDate> start = request.queryParam(START_DATE_KEY).map(LocalDate::parse);
        final Optional<LocalDate> end = request.queryParam(END_DATE_KEY).map(LocalDate::parse);
        final Boolean daily = request.queryParam(DAILY_KEY).map(Boolean::valueOf).orElse(false);
        final Boolean monthly = request.queryParam(MONTHLY_KEY).map(Boolean::valueOf).orElse(false);
        final Boolean yearly = request.queryParam(YEARLY_KEY).map(Boolean::valueOf).orElse(false);
        if (!start.isPresent()) {
            addError(MISSING_QUERY_PARAM_PREFIX + START_DATE_KEY);
        }
        if (!end.isPresent()) {
            addError(MISSING_QUERY_PARAM_PREFIX + END_DATE_KEY);
        }
        if (!periodCheck(start, end)) {
            addError("Something when wrong with the entered period. The start date should be before the end date.");
        }
        if (!errors.isEmpty()) {
            return badRequestError(errors);
        }
        return query(
                () -> DashboardQuery.builder()
                .userId(userId)
                .start(start.get())
                .end(end.get())
                .daily(daily)
                .monthly(monthly)
                .yearly(yearly)
                .build(),
                DashboardView.class);

    }

    private Boolean periodCheck(Optional<LocalDate> start, Optional<LocalDate> end) {
        return (start.isPresent() && end.isPresent()) && (start.get().equals(end.get()) || start.get().isBefore(end.get()));
    }

    @org.axonframework.queryhandling.QueryHandler
    public Object dashboardGet(DashboardQuery query) {
        return builder.build(DashboardQuery.from(query));
    }
}
