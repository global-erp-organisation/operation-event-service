package com.ia.operation.repositories;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Operation;

import reactor.core.publisher.Flux;

public interface OperationRepository extends ReactiveMongoRepository<Operation, String> {

    Flux<Operation> findByPeriod_description(String month);

    Flux<Operation> findByPeriod_year(int year);

    Flux<Operation> findByOperationDate(LocalDate date);

    Flux<Operation> findByPeriod_descriptionAndAccount_id(String month, String accountId);

    Flux<Operation> findByPeriod_yearAndAccount_id(int year, String accountId);

    Flux<Operation> findByOperationDateAndAccount_id(LocalDate date, String accountId);

}
