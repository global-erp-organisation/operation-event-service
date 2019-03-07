package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.views.MonthlyHistoryView;

import reactor.core.publisher.Flux;

public interface MonthlyHistoryRepository extends ReactiveMongoRepository<MonthlyHistoryView, String> {
    Flux<MonthlyHistoryView> findByMonthAndAccount_id(String month, String accountId);
}
