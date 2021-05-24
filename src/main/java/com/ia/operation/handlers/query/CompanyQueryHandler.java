package com.ia.operation.handlers.query;

import com.ia.operation.documents.Company;
import com.ia.operation.handlers.QueryHandler;
import com.ia.operation.queries.company.CompanyGetAllQuery;
import com.ia.operation.queries.company.CompanyGetQuery;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CompanyQueryHandler extends QueryHandler {
    public CompanyQueryHandler(QueryGateway gateway) {
        super(gateway);
    }

    public Mono<ServerResponse> companyGet(ServerRequest request) {
        final String companyId = request.pathVariable(COMPANY_ID_KEY);
        return query(() -> CompanyGetQuery.builder().companyId(companyId).build(), Company.class);
    }

    public Mono<ServerResponse> companyGetAll(ServerRequest request) {
        return queryList(CompanyGetAllQuery::new, Company.class);
    }
}
