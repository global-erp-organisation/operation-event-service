package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Projection;

import reactor.core.publisher.Flux;

public interface ProjectionRepository extends ReactiveMongoRepository<Projection, String> {
    Flux<Projection> findByAccount_IdAndPeriod_yearOrderByPeriod_start(String accountId, String year);

    Flux<Projection> findByAccount_User_IdAndPeriod_yearOrderByPeriod_start(String userId, String year);
    
    Flux<Projection> findByAccount_IdAndPeriod_IdOrderByAccount_description(String accountId, String periodId);
}
