package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.views.YearlyHistoryView;

import reactor.core.publisher.Flux;

public interface YearlyHistoryRepository extends ReactiveMongoRepository<YearlyHistoryView, String> {
    Flux<YearlyHistoryView> findByYearAndAccount_id(int year, String accountId);
}
