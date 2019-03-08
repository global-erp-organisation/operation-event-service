package com.ia.operation.handlers.query;

import java.time.LocalDate;

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
        return request.bodyToMono(DashboardQuery.class).map(body -> {
            if (!periodCheck(body.getStart(), body.getEnd())) {
                return ServerResponse.badRequest().body(Mono.just("Something when wrong with the entered period."), String.class);
            }
            return queryComplete(() -> DashboardQuery.builder().userId(userId).start(body.getStart()).end(body.getEnd()).build(), DashboardView.class,
                    gateway);
        }).flatMap(response -> response).switchIfEmpty(ServerResponse.badRequest().body(Mono.just(MISSING_REQUEST_BODY_KEY), String.class));

    }

    private Boolean periodCheck(LocalDate start, LocalDate end) {
        return (start != null && end != null) && (start.equals(end) || start.isBefore(end));
    }

    @QueryHandler
    public Object getDashBoard(DashboardQuery query) {
        return builder.build(DashboardQuery.from(query));
    }
}
