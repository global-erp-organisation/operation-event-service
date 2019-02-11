package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.views.YearlyHistoryView;
import com.ia.operation.enums.OperationType;

import reactor.core.publisher.Flux;

public interface YearlyHistoryRepository extends ReactiveMongoRepository<YearlyHistoryView, String> {
    Flux<YearlyHistoryView> findByYearAndType(int year, OperationType type);
}
