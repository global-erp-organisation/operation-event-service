package com.ia.operation.handlers.query;

import com.ia.operation.documents.views.DashboardView;
import com.ia.operation.handlers.QueryHandler;
import com.ia.operation.helper.history.DashboardBuilder;
import com.ia.operation.queries.account.DashboardQuery;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

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
        final LocalDate start = request.queryParam(START_DATE_KEY).map(LocalDate::parse).orElseGet(() -> {
            addError(MISSING_QUERY_PARAM_PREFIX + START_DATE_KEY);
            return LocalDate.now();
        });
        final LocalDate end = request.queryParam(END_DATE_KEY).map(LocalDate::parse).orElseGet(() -> {
            addError(MISSING_QUERY_PARAM_PREFIX + END_DATE_KEY);
            return LocalDate.now();
        });
        final Boolean daily = request.queryParam(DAILY_KEY).map(Boolean::valueOf).orElse(false);
        final Boolean monthly = request.queryParam(MONTHLY_KEY).map(Boolean::valueOf).orElse(false);
        final Boolean yearly = request.queryParam(YEARLY_KEY).map(Boolean::valueOf).orElse(false);
        if (!periodCheck(start, end)) {
            addError("Something when wrong with the entered period. The start date should be before the end date.");
        }
        if (!errors.isEmpty()) {
            return badRequestError(errors);
        }
        return query(
                () -> DashboardQuery.builder()
                        .userId(userId)
                        .start(start)
                        .end(end)
                        .daily(daily)
                        .monthly(monthly)
                        .yearly(yearly)
                        .build(),
                DashboardView.class);

    }

    private Boolean periodCheck(LocalDate start, LocalDate end) {
        return start.equals(end) || start.isBefore(end);
    }

    @org.axonframework.queryhandling.QueryHandler
    public CompletableFuture<DashboardView> dashboardGet(DashboardQuery query) {
        return builder.build(DashboardQuery.from(query)).toFuture();
    }
}
