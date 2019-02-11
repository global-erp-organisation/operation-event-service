package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Projection;

import reactor.core.publisher.Flux;

public interface ProjectionRepository extends ReactiveMongoRepository<Projection, String> {
    Flux<Projection> findByOperation_IdAndOperation_UserId(String operationId, String userId);

    Flux<Projection> findByPeriod_IdAndOperation_UserIdOrderByOperation_description(String periodId, String userId);

    Flux<Projection> findByOperation_User_Id(String userId);
}
