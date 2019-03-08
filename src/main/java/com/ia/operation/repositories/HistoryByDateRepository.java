package com.ia.operation.repositories;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.views.DailyHistoryView;

import reactor.core.publisher.Flux;

public interface HistoryByDateRepository extends ReactiveMongoRepository<DailyHistoryView, String> {
    Flux<DailyHistoryView> findBydateAndAccount_id(LocalDate date, String accountId);
}
