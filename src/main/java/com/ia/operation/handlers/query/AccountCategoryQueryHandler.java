package com.ia.operation.handlers.query;

import com.ia.operation.handlers.QueryHandler;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.AccountCategory;
import com.ia.operation.queries.category.CategoryGetAllQuery;
import com.ia.operation.queries.category.CategoryGetQuery;

import reactor.core.publisher.Mono;

@Component
public class AccountCategoryQueryHandler extends QueryHandler  {

    public AccountCategoryQueryHandler(QueryGateway gateway) {
        super(gateway);
    }
    public Mono<ServerResponse> accountCategoryGet(ServerRequest request) {
        final String categoryId = request.pathVariable(CATEGORY_ID_KEY);
        return query(() -> CategoryGetQuery.builder().categoryId(categoryId).build(), AccountCategory.class);
    }

    public Mono<ServerResponse> accountCategoryGetAll(ServerRequest request) {
        return query(CategoryGetAllQuery::new, AccountCategory.class);
    }
}
