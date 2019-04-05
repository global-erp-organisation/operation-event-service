package com.ia.operation.handlers.query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        final Boolean daily = request.queryParam(DAILY_KEY).map(s -> Boolean.valueOf(s)).orElse(false);
        final Boolean monthly = request.queryParam(MONTHLY_KEY).map(s -> Boolean.valueOf(s)).orElse(false);
        final Boolean yearly = request.queryParam(YEARLY_KEY).map(s -> Boolean.valueOf(s)).orElse(false);
        final List<String> errors = new ArrayList<>();
        if (userId == null) {
            errorItemAdd(errors, MISSING_PATH_VARIABLE_PREFIX + USER_ID_KEY);
        }
        if (!start.isPresent()) {
            errorItemAdd(errors, MISSING_QUERY_PARAM_PREFIX + START_DATE_KEY);
        }
        if (!end.isPresent()) {
            errorItemAdd(errors, MISSING_QUERY_PARAM_PREFIX + END_DATE_KEY);
        }
        if (!periodCheck(start, end)) {
            errorItemAdd(errors, "Something when wrong with the entered period. The start date should be before the end date.");
        }
        if (!errors.isEmpty()) {
            return badRequestError(errors);
        }
        return queryComplete(
                () -> DashboardQuery.builder()
                .userId(userId)
                .start(start.get())
                .end(end.get())
                .daily(daily)
                .monthly(monthly)
                .yearly(yearly)
                .build(),
                DashboardView.class, gateway);

    }

    private Boolean periodCheck(Optional<LocalDate> start, Optional<LocalDate> end) {
        return (start.isPresent() && end.isPresent()) && (start.get().equals(end.get()) || start.get().isBefore(end.get()));
    }

    @QueryHandler
    public Object dashboardGet(DashboardQuery query) {
        return builder.build(DashboardQuery.from(query));
    }
}
