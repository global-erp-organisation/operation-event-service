package com.ia.operation.repositories;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.views.YearlyHistoryView;

import reactor.core.publisher.Flux;

public interface YearlyHistoryRepository extends ReactiveMongoRepository<YearlyHistoryView, String>, HistoryQuery<YearlyHistoryView> {
    Flux<YearlyHistoryView> findByYearAndAccount_id(int year, String accountId);
    Flux<YearlyHistoryView> findByAccount_User_IdAndDateBetweenOrderByDate(String userId, LocalDate start, LocalDate end);
}
