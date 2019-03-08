package com.ia.operation.repositories;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.views.DailyHistoryView;

import reactor.core.publisher.Flux;

public interface DailyHistoryRepository extends ReactiveMongoRepository<DailyHistoryView, String> {
    Flux<DailyHistoryView> findBystartAndAccount_id(LocalDate start, String accountId);

    //@Query(value = "{ 'account.user.id' : ?0, 'date' : { $gt : ?1 , $lt : ?2 } }")
    //Flux<DailyHistoryView> findByUserPerPeriod(String userId, LocalDate start, LocalDate end);
    Flux<DailyHistoryView> findByAccount_userIdAndDateBetween(String userId, LocalDate start, LocalDate end);
}
