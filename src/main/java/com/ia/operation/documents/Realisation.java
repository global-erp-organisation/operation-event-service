package com.ia.operation.documents;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.events.created.RealisationCreatedEvent;
import com.ia.operation.events.updated.RealisationUpdatedEvent;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document
public class Realisation {
    @Id
    private String id;
    private String description;
    private Operation operation;
    private LocalDate operationDate;
    private Period period;
    private BigDecimal amount;

    
    public static Realisation of(RealisationCreatedEvent event, Period period, Operation operation) {
        return Realisation.builder()
                .id(event.getId())
                .description(event.getDescription())
                .operation(operation)
                .operationDate(event.getOperationDate())
                .period(period)
                .amount(event.getAmount())
                .build();
    }
    
    public static Realisation of(RealisationUpdatedEvent event, Period period, Operation operation) {
        return Realisation.builder()
                .id(event.getId())
                .description(event.getDescription())
                .operation(operation)
                .operationDate(event.getOperationDate())
                .period(period)
                .amount(event.getAmount())
                .build();
    }

    
    public static RealisationBuilder from(Realisation event) {
        return Realisation.builder()
                .id(event.getId())
                .description(event.getDescription())
                .operation(event.getOperation())
                .operationDate(event.getOperationDate())
                .period(event.getPeriod())
                .amount(event.getAmount());
    }

}
