package com.ia.operation.documents;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.events.created.RealisationCreatedEvent;
import com.ia.operation.events.updated.OperationUpdatedEvent;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document
public class Operation {
    @Id
    private String id;
    private String description;
    private Account account;
    private LocalDate operationDate;
    private Period period;
    private BigDecimal amount;

    
    public static Operation of(RealisationCreatedEvent event, Period period, Account account) {
        return Operation.builder()
                .id(event.getId())
                .description(event.getDescription())
                .account(account)
                .operationDate(event.getOperationDate())
                .period(period)
                .amount(event.getAmount())
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
                .build();
    }

    
    public static OperationBuilder from(Operation event) {
        return Operation.builder()
                .id(event.getId())
                .description(event.getDescription())
                .account(event.getAccount())
                .operationDate(event.getOperationDate())
                .period(event.getPeriod())
                .amount(event.getAmount());
    }

}
