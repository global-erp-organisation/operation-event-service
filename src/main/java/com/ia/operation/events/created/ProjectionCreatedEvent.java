package com.ia.operation.events.created;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@SuppressWarnings("serial")
@Value
@Builder
public class ProjectionCreatedEvent implements Serializable {
    private String id;
    private String accountId;
    private BigDecimal amount;
    private String periodId;

}
