package com.ia.operation.handlers.query;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.documents.Company;
import com.ia.operation.handlers.Handler;
import com.ia.operation.queries.company.CompanyGetAllQuery;
import com.ia.operation.queries.company.CompanyGetQuery;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CompanyQueryHandler implements Handler {
    private final QueryGateway gateway;

    public Mono<ServerResponse> companyGet(ServerRequest request) {
        final String companyId = request.pathVariable(COMPANY_ID_KEY);
        if (companyId == null) {
            return badRequestError(COMPANY_ID_KEY);
        }
        return queryComplete(() -> CompanyGetQuery.builder().companyId(companyId).build(), Company.class, gateway);
    }

    public Mono<ServerResponse> companyGetAll(ServerRequest request) {
        return queryComplete(() -> new CompanyGetAllQuery(), Company.class, gateway);
    }
}
