package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.views.MonthlyHistoryView;
import com.ia.operation.enums.OperationType;

import reactor.core.publisher.Flux;

public interface MonthlyHistoryRepository extends ReactiveMongoRepository<MonthlyHistoryView, String> {
    Flux<MonthlyHistoryView> findByMonthAndType(String month, OperationType type);
}
