package com.ia.operation.events.updated;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectionUpdatedEvent {
    private String id;
    private String operationId;
    private BigDecimal amount;
    private String periodId;

}
