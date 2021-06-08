package com.ia.operation.documents;

import com.ia.operation.enums.OperationType;
import com.ia.operation.events.created.ProjectionCreatedEvent;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
@Builder
@Data
public class Projection {
    @Id
    private String id;
    //@ManyToOne(fetch = FetchType.EAGER)
    private Account account;
    private BigDecimal amount;
    //@ManyToOne(fetch = FetchType.EAGER)
    private Period period;
    private OperationType operationType;
    
    public static Projection of(ProjectionCreatedEvent event, Account account, Period period) {
        return Projection.builder()
                .id(event.getId())
                .account(account)
                .amount(event.getAmount())
                .period(period)
                .operationType(event.getOperationType())
                .build();
    }
}
