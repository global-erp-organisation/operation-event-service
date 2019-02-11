package com.ia.operation.handlers.query;

import java.util.concurrent.ExecutionException;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ia.operation.documents.Company;
import com.ia.operation.queries.company.CompanyByDescriptionQuery;
import com.ia.operation.queries.company.CompanyGetAllQuery;
import com.ia.operation.queries.company.CompanyGetQuery;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CompanyQueryHandler {
    private final QueryGateway gateway;

    @GetMapping(value = "/companies/{companyId}")
    public Mono<Company> companyGet(@PathVariable String companyId) throws InterruptedException, ExecutionException {
        return (Mono<Company>) gateway.query(CompanyGetQuery.builder().companyId(companyId).build(), Object.class).get();
    }

    @GetMapping(value = "/companies")
    public Flux<Company> companyGetAll() throws InterruptedException, ExecutionException {
        return (Flux<Company>) gateway.query(new CompanyGetAllQuery(), Object.class).get();
    }
    
    @PostMapping(value = "/companies/description")
    public Flux<Company> companyByDescription(@RequestBody CompanyByDescriptionQuery request) throws InterruptedException, ExecutionException {
        return (Flux<Company>) gateway.query(request, Object.class).get();
    }

}
