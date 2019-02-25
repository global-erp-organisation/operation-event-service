package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Period;

import reactor.core.publisher.Flux;

public interface PeriodRepository extends ReactiveMongoRepository<Period, String> {

    Flux<Period> findByYear(String year);
}
