package com.ia.operation.repositories;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.Operation;
import com.ia.operation.enums.AccountType;

import reactor.core.publisher.Flux;

public interface OperationRepository extends ReactiveMongoRepository<Operation, String> {

    Flux<Operation> findByPeriod_description(String month);

    Flux<Operation> findByPeriod_year(int year);

    Flux<Operation> findByOperationDate(LocalDate date);

    Flux<Operation> findByPeriod_descriptionAndAccount_accountType(String month, AccountType type);

    Flux<Operation> findByPeriod_yearAndAccount_accountType(int year, AccountType type);

    Flux<Operation> findByOperationDateAndAccount_accountType(LocalDate date, AccountType type);

}
