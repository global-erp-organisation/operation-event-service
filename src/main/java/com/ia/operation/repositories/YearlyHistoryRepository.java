package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.views.YearlyHistoryView;
import com.ia.operation.enums.AccountType;

import reactor.core.publisher.Flux;

public interface YearlyHistoryRepository extends ReactiveMongoRepository<YearlyHistoryView, String> {
    Flux<YearlyHistoryView> findByYearAndType(int year, AccountType type);
}
