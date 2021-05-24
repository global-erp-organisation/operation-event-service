package com.ia.operation.handlers.event;

import com.ia.operation.documents.Company;
import com.ia.operation.events.created.CompanyCreatedEvent;
import com.ia.operation.events.deleted.CompanyDeletedEvent;
import com.ia.operation.events.updated.CompanyUpdatedEvent;
import com.ia.operation.queries.company.CompanyGetAllQuery;
import com.ia.operation.queries.company.CompanyGetQuery;
import com.ia.operation.repositories.CompanyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@ProcessingGroup(value = "company-handler")
@AllArgsConstructor
@Slf4j
public class CompanyEventHandler {
    private final CompanyRepository companyRepository;

    @EventHandler
    public void on(CompanyCreatedEvent event) {
        log.info("Event recieved: [{}]", event);
        companyRepository.save(Company.of(event)).subscribe(c -> log.info("Company succesfully saved: [{}]", c));
    }

    @EventHandler
    public void on(CompanyUpdatedEvent event) {
        log.info("Event recieved: [{}]", event);
        companyRepository.save(Company.of(event)).subscribe(c -> log.info("Company succesfully saved: [{}]", c));
    }

    @EventHandler
    public void on(CompanyDeletedEvent event) {
        log.info("Event recieved: [{}]", event);
        companyRepository.deleteById(event.getCompanyId()).subscribe(c -> log.info("Company with id [{}] successfully removed", event.getCompanyId()));
    }

    @QueryHandler
    public CompletableFuture<Company> companyGet(CompanyGetQuery query) {
        log.info("A new query for companyGet have been recieved: {}", query);
        return companyRepository.findById(query.getCompanyId()).toFuture();
    }

    @QueryHandler
    public CompletableFuture<List<Company>> companyGetAll(CompanyGetAllQuery query) {
        log.info("A new query for companyGetAll have been recieved: {}", query);
        return  companyRepository.findAll().collectList().toFuture();
    }
}
