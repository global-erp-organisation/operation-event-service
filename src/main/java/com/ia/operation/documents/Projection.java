package com.ia.operation.documents;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.events.created.ProjectionCreatedEvent;

import io.github.kaiso.relmongo.annotation.JoinProperty;
import io.github.kaiso.relmongo.annotation.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
public class Projection {
    @Id
    private String id;
    @ManyToOne
    @JoinProperty
    private Account account;
    private BigDecimal amount;
    private Period period;
    
    public static Projection of(ProjectionCreatedEvent event, Account account, Period period) {
        return Projection.builder()
                .id(event.getId())
                .account(account)
                .amount(event.getAmount())
                .period(period)
                .build();
    }
}
