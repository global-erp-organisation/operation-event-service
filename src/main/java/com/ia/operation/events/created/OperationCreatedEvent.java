package com.ia.operation.events.created;


import java.math.BigDecimal;

import com.ia.operation.enums.OperationType;
import com.ia.operation.enums.RecurringMode;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OperationCreatedEvent {
    private String id;
    private String description;
    private String userId;
    private OperationType operationType;
    private RecurringMode recurringMode;
    private BigDecimal defaultAmount;
    private String categoryId;
}
