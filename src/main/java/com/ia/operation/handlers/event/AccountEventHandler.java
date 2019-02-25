package com.ia.operation.handlers.event;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.ia.operation.documents.Account;
import com.ia.operation.events.created.AccountCreatedEvent;
import com.ia.operation.events.deleted.AccountDeletedEvent;
import com.ia.operation.events.updated.AccountUpdatedEvent;
import com.ia.operation.queries.account.AccountGetAllQuery;
import com.ia.operation.queries.account.AccountGetByIdQuery;
import com.ia.operation.repositories.AccountCategoryRepository;
import com.ia.operation.repositories.AccountRepository;
import com.ia.operation.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@ProcessingGroup("operation-handler")
public class AccountEventHandler {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountCategoryRepository accountCategoryRepository;

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("event recieved: value=[{}]", event);
        userRepository.findById(event.getUserId()).subscribe(user -> {
            accountCategoryRepository.findById(event.getCategoryId()).subscribe(category -> {
                accountRepository.save(Account.of(event, user, category)).subscribe(o -> {
                    log.info("Account successfully saved. value=[{}]", o);
                });
            });
        });
    }

    @EventHandler
    public void on(AccountUpdatedEvent event) {
        log.info("event recieved: value=[{}]", event);
        userRepository.findById(event.getUserId()).subscribe(user -> {
            accountCategoryRepository.findById(event.getCategoryId()).subscribe(category -> {
                accountRepository.save(Account.of(event, user, category)).subscribe(o -> {
                    log.info("Account successfully saved. value=[{}]", o);
                });
            });
        });
    }

    @EventHandler
    public void on(AccountDeletedEvent event) {
        log.info("event recieved: value=[{}]", event);
        accountRepository.deleteById(event.getId()).subscribe(e -> {
            log.info("Account successfully removed. value=[{}]", event.getId());
        });
    }

    @QueryHandler
    public Object accountGet(AccountGetByIdQuery query) {
        log.info("A new query for AccountGet have been recieved: {}", query);
        return accountRepository.findById(query.getAccountId());
    }

    @QueryHandler
    public Object accountGetByUser(AccountGetAllQuery query) {
        log.info("A new AccountGetByUser by user query have been recieved., query=[{}]", query);
        return accountRepository.findByUser_id(query.getUserId());
    }

}