package com.ia.operation.events.updated;

import java.math.BigDecimal;

import com.ia.operation.enums.OperationType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectionUpdatedEvent {
    private String id;
    private String accountId;
    private BigDecimal amount;
    private String periodId;
    private String year;
    private OperationType operationType;
}
