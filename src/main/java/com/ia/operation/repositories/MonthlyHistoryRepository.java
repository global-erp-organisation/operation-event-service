package com.ia.operation.repositories;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.views.MonthlyHistoryView;

import reactor.core.publisher.Flux;

public interface MonthlyHistoryRepository extends ReactiveMongoRepository<MonthlyHistoryView, String>, HistoryQuery<MonthlyHistoryView> {
    
    Flux<MonthlyHistoryView> findByMonthAndAccount_id(String month, String accountId);

    Flux<MonthlyHistoryView> findByAccount_User_IdAndDateBetweenOrderByDate(String userId, LocalDate start, LocalDate end);
}
