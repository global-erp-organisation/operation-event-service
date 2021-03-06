package com.ia.operation.handlers.event;

import com.ia.operation.documents.Account;
import com.ia.operation.documents.AccountCategory;
import com.ia.operation.documents.User;
import com.ia.operation.events.created.AccountCreatedEvent;
import com.ia.operation.events.deleted.AccountDeletedEvent;
import com.ia.operation.events.updated.AccountUpdatedEvent;
import com.ia.operation.helper.ProjectionGenerator;
import com.ia.operation.queries.account.AccountGetAllQuery;
import com.ia.operation.queries.account.AccountGetByIdQuery;
import com.ia.operation.repositories.AccountCategoryRepository;
import com.ia.operation.repositories.AccountRepository;
import com.ia.operation.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
@ProcessingGroup("account-handler")
public class AccountEventHandler {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountCategoryRepository accountCategoryRepository;
    private final ProjectionGenerator generator;
    
    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("event recieved: value=[{}]", event);
        final Mono<User> user = userRepository.findById(event.getUserId());
        final Mono<AccountCategory> category = accountCategoryRepository.findById(event.getCategoryId());

        user.subscribe(u -> category.subscribe(c -> accountRepository.save(Account.of(event, u, c)).subscribe(account -> {
            generator.generate(account);
            log.info("Account successfully saved. value=[{}]", account);
        })));
    }

    @EventHandler
    public void on(AccountUpdatedEvent event) {
        log.info("event recieved: value=[{}]", event);
        final Mono<User> user = userRepository.findById(event.getUserId());
        final Mono<AccountCategory> category = accountCategoryRepository.findById(event.getCategoryId());
        user.subscribe(u -> category.subscribe(c -> accountRepository.save(Account.of(event, u, c)).subscribe(account -> log.info("Account successfully updated. value=[{}]", account))));
    }

    @EventHandler
    public void on(AccountDeletedEvent event) {
        log.info("event recieved: value=[{}]", event);
        accountRepository.deleteById(event.getId()).subscribe(e -> log.info("Account successfully removed. value=[{}]", event.getId()));
    }

    @QueryHandler
    public CompletableFuture<Account> accountGet(AccountGetByIdQuery query) {
        log.info("A new query for AccountGet have been recieved: {}", query);
        return accountRepository.findById(query.getAccountId()).toFuture();
    }

    @QueryHandler
    public CompletableFuture<List<Account>> accountGetByUser(AccountGetAllQuery query) {
        log.info("A new AccountGetByUser by user query have been recieved., query=[{}]", query);
        return accountRepository.findByUser_id(query.getUserId()).collectList().toFuture();
    }

}
