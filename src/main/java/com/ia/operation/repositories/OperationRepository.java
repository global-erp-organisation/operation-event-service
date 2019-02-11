package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Operation;

import reactor.core.publisher.Flux;

public interface OperationRepository extends ReactiveMongoRepository<Operation, String> {
    Flux<Operation> findByUser_id(String userId);
}
