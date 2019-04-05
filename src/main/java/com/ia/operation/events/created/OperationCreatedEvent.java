package com.ia.operation.events.created;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ia.operation.enums.OperationType;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class OperationCreatedEvent {
    private String id;
    private String description;
    private String accountId;
    private LocalDate operationDate;
    private BigDecimal amount;
    private OperationType operationType;

}
