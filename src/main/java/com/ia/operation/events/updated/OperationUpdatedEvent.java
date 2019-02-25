package com.ia.operation.events.updated;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class OperationUpdatedEvent {
    private String id;
    private String description;
    private String accountId;
    private LocalDate operationDate;
    private String periodId;
    private BigDecimal amount;
}
