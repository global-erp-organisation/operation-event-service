package com.ia.operation.documents;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.enums.OperationType;
import com.ia.operation.events.created.OperationCreatedEvent;
import com.ia.operation.events.updated.OperationUpdatedEvent;

import io.github.kaiso.relmongo.annotation.FetchType;
import io.github.kaiso.relmongo.annotation.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document
public class Operation {
    @Id
    private String id;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;
    private LocalDate operationDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private Period period;
    private BigDecimal amount;
    @JsonProperty("operation_type")
    private OperationType operationType;


    
    public static Operation of(OperationCreatedEvent event, Period period, Account account) {
        return Operation.builder()
                .id(event.getId())
                .description(event.getDescription())
                .account(account)
                .operationDate(event.getOperationDate())
                .period(period)
                .amount(event.getAmount())
                .operationType(event.getOperationType())
                .build();
    }
    
    public static Operation of(Operation event, Period period, Account account) {
        return Operation.builder()
                .id(event.getId())
                .description(event.getDescription())
                .account(account)
                .operationDate(event.getOperationDate())
                .period(period)
                .amount(event.getAmount())
                .operationType(event.getOperationType())
                .build();
    }

    
    public static Operation of(OperationUpdatedEvent event, Period period, Account account) {
        return Operation.builder()
                .id(event.getId())
                .description(event.getDescription())
                .account(account)
                .operationDate(event.getOperationDate())
                .period(period)
                .amount(event.getAmount())
                .operationType(event.getOperationType())
                .build();
    }

    
    public static OperationBuilder from(Operation event) {
        return Operation.builder()
                .id(event.getId())
                .description(event.getDescription())
                .account(event.getAccount())
                .operationDate(event.getOperationDate())
                .period(event.getPeriod())
                .amount(event.getAmount())
                .operationType(event.getOperationType());
    }

}
