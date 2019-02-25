package com.ia.operation.documents;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.enums.OperationType;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.events.created.AccountCreatedEvent;
import com.ia.operation.events.updated.AccountUpdatedEvent;

import io.github.kaiso.relmongo.annotation.FetchType;
import io.github.kaiso.relmongo.annotation.JoinProperty;
import io.github.kaiso.relmongo.annotation.OneToMany;
import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
public class Account {
    @Id
    private String id;
    private String description;
    private OperationType operationType;
    private RecurringMode recurringMode;
    private BigDecimal defaultAmount;
    @DBRef
    private AccountCategory category;
   
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER)
    @JoinProperty(name = "operations")
    private Collection<Operation> operations = new ArrayList<Operation>();
    
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER)
    @JoinProperty(name = "projections")
    private Collection<Projection> projections = new ArrayList<Projection>();
    private User user;

    public static Account of(AccountCreatedEvent event, User user, AccountCategory category) {
        return Account.builder()
                .id(event.getId())
                .user(user)
                .description(event.getDescription())
                .recurringMode(event.getRecurringMode())
                .operationType(event.getOperationType())
                .defaultAmount(event.getDefaultAmount())
                .category(category)
                .build();
    }
    
    public static Account of(AccountUpdatedEvent event, User user, AccountCategory category) {
        return Account.builder()
                .id(event.getId())
                .user(user)
                .description(event.getDescription())
                .recurringMode(event.getRecurringMode())
                .operationType(event.getOperationType())
                .defaultAmount(event.getDefaultAmount())
                .category(category)
                .build();
    }

}
