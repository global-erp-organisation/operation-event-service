package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Company;

import reactor.core.publisher.Flux;

public interface CompanyRepository extends ReactiveMongoRepository<Company, String> {
    Flux<Company> findByDescription(String description);
}
