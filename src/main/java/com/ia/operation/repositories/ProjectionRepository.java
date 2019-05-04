package com.ia.operation.repositories;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Projection;
import com.ia.operation.enums.AccountType;

import reactor.core.publisher.Flux;

public interface ProjectionRepository extends ReactiveMongoRepository<Projection, String> {
    Flux<Projection> findByAccount_IdAndPeriod_yearOrderByPeriod_start(String accountId, String year);

    Flux<Projection> findByAccount_User_IdAndPeriod_yearOrderByPeriod_start(String userId, String year);

    Flux<Projection> findByAccount_IdAndPeriod_IdOrderByAccount_description(String accountId, String periodId);

    @Query(value = "{ 'account.user.id' : ?0, 'period.start' : { $gt : ?1 , $lt : ?2 }, accountType: ?3 }")
    Flux<Projection> findByUserAndPeriod(String userId, LocalDate start, LocalDate end, AccountType accountType);
}
