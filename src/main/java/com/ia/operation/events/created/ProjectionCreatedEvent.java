package com.ia.operation.events.created;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectionCreatedEvent implements Serializable {
    private String id;
    private String accountId;
    private BigDecimal amount;
    private String periodId;
}
