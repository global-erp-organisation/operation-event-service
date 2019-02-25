package com.ia.operation.repositories;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Operation;
import com.ia.operation.enums.OperationType;

import reactor.core.publisher.Flux;

public interface OperationRepository extends ReactiveMongoRepository<Operation, String> {

    Flux<Operation> findByPeriod_description(String month);

    Flux<Operation> findByPeriod_year(int year);

    Flux<Operation> findByOperationDate(LocalDate date);

    Flux<Operation> findByPeriod_descriptionAndAccount_operationType(String month, OperationType type);

    Flux<Operation> findByPeriod_yearAndAccount_operationType(int year, OperationType type);

    Flux<Operation> findByOperationDateAndAccount_operationType(LocalDate date, OperationType type);

}
