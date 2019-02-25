package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Projection;

import reactor.core.publisher.Flux;

public interface ProjectionRepository extends ReactiveMongoRepository<Projection, String> {
    Flux<Projection> findByAccount_IdAndAccount_UserIdAndPeriod_year(String accountId, String userId, String year);

    Flux<Projection> findByAccount_User_IdAndPeriod_year(String userId, String year);
}
