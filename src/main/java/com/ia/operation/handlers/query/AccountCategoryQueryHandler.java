package com.ia.operation.handlers.query;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.AccountCategory;
import com.ia.operation.handlers.Handler;
import com.ia.operation.queries.category.CategoryGetAllQuery;
import com.ia.operation.queries.category.CategoryGetQuery;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountCategoryQueryHandler implements Handler {
    private final QueryGateway gateway;

    public Mono<ServerResponse> accountCategoryGet(ServerRequest request) {
        final String categoryId = request.pathVariable(CATEGORY_ID);
        if (categoryId == null) {
            return badRequestComplete(() -> CATEGORY_ID);
        }
        return queryComplete(() -> CategoryGetQuery.builder().categoryId(categoryId).build(), AccountCategory.class, gateway);
    }

    public Mono<ServerResponse> accountCategoryGetAll(ServerRequest request) {
        return queryComplete(() -> new CategoryGetAllQuery(), AccountCategory.class, gateway);
    }
}
