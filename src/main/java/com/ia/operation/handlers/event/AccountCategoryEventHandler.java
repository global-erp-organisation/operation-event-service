package com.ia.operation.handlers.event;

import com.ia.operation.documents.AccountCategory;
import com.ia.operation.events.created.AccountCategoryCreatedEvent;
import com.ia.operation.events.deleted.AccountCategoryDeletedEvent;
import com.ia.operation.events.updated.AccountCategoryUpdatedEvent;
import com.ia.operation.queries.category.CategoryGetAllQuery;
import com.ia.operation.queries.category.CategoryGetQuery;
import com.ia.operation.repositories.AccountCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@ProcessingGroup("operation-category-handler")
@RequiredArgsConstructor
@Slf4j
public class AccountCategoryEventHandler {

    private final AccountCategoryRepository accountCategoryRepository;

    @EventHandler
    public void on(AccountCategoryCreatedEvent event) {
        log.info("Event recieved: [{}]", event);
        accountCategoryRepository.save(AccountCategory.from(event).build()).subscribe(c -> log.info("Account category succesfully saved value: [{}]", c));
    }

    @EventHandler
    public void on(AccountCategoryUpdatedEvent event) {
        log.info("Event recieved: [{}]", event);
        accountCategoryRepository.save(AccountCategory.from(event).build()).subscribe(c -> log.info("Account category succesfully updated value: [{}]", c));
    }

    @EventHandler
    public void on(AccountCategoryDeletedEvent event) {
        log.info("Event recieved: [{}]", event);
        accountCategoryRepository.deleteById(event.getCategoryId()).subscribe(c -> {
            log.info("Account category succesfully removed value: [{}]", c);
        });
    }

    @QueryHandler
    public CompletableFuture<List<AccountCategory>> cagetoryGetAll(CategoryGetAllQuery query) {
        return accountCategoryRepository.findAll().collectList().toFuture();
    }

    @QueryHandler
    public CompletableFuture<AccountCategory> cagetoryGet(CategoryGetQuery query) {
        return accountCategoryRepository.findById(query.getCategoryId()).toFuture();
    }
}
