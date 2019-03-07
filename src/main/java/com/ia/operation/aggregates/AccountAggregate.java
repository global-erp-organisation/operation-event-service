package com.ia.operation.aggregates;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.operation.commands.creation.AccountCreationCmd;
import com.ia.operation.commands.delete.AccountDeletionCmd;
import com.ia.operation.commands.update.AccountUpdateCmd;
import com.ia.operation.enums.AccountType;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.events.created.AccountCreatedEvent;
import com.ia.operation.events.deleted.AccountDeletedEvent;
import com.ia.operation.events.updated.AccountUpdatedEvent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@NoArgsConstructor
public class AccountAggregate {
    @AggregateIdentifier
    private String id;
    private String description;
    private AccountType accountType;
    private String userId;
    private RecurringMode recurringMode;
    private BigDecimal defaultAmount;
    private String categoryId;

    @CommandHandler
    public AccountAggregate(AccountCreationCmd cmd) {
        AggregateLifecycle.apply(AccountCreationCmd.enventFrom(cmd).build());
    }

    @EventSourcingHandler
    public void onOperationCreated(AccountCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.accountType = event.getAccountType();
        this.userId = event.getUserId();
        this.recurringMode = event.getRecurringMode();
        this.defaultAmount = event.getDefaultAmount();
        this.categoryId = event.getCategoryId();
    }

    @CommandHandler
    public void handleOperationUpdateCmd(AccountUpdateCmd cmd) {
        AggregateLifecycle.apply(AccountUpdateCmd.eventFrom(cmd).build());
    }

    @EventSourcingHandler
    public void onOperationCreated(AccountUpdatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.accountType = event.getAccountType();
        this.userId = event.getUserId();
        this.recurringMode = event.getRecurringMode();
        this.defaultAmount = event.getDefaultAmount();
        this.categoryId = event.getCategoryId();
    }

    @CommandHandler
    public void handleOperationDeletionCmd(AccountDeletionCmd cmd) {
        AggregateLifecycle.apply(AccountDeletedEvent.builder().id(cmd.getId()).build());
        AggregateLifecycle.markDeleted();
    }

    @EventSourcingHandler
    public void onOperationDeleted(AccountDeletedEvent event) {
        this.id = event.getId();
    }
}
