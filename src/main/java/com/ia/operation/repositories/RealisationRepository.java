package com.ia.operation.repositories;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Realisation;
import com.ia.operation.enums.OperationType;

import reactor.core.publisher.Flux;

public interface RealisationRepository extends ReactiveMongoRepository<Realisation, String> {

    Flux<Realisation> findByPeriod_description(String month);

    Flux<Realisation> findByPeriod_year(int year);

    Flux<Realisation> findByOperationDate(LocalDate date);

    Flux<Realisation> findByPeriod_descriptionAndOperation_operationType(String month, OperationType type);

    Flux<Realisation> findByPeriod_yearAndOperation_operationType(int year, OperationType type);

    Flux<Realisation> findByOperationDateAndOperation_operationType(LocalDate date, OperationType type);

}
