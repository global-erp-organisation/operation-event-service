package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Account;
import com.ia.operation.enums.AccountType;

import reactor.core.publisher.Flux;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Flux<Account> findByUser_id(String userId);
    Flux<Account> findByUser_idAndAccountType(String userId, AccountType accountType);
}
