package com.ia.operation.aggregates;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.operation.commands.creation.OperationCreationCmd;
import com.ia.operation.commands.delete.OperationDeletionCmd;
import com.ia.operation.commands.update.OperationUpdateCmd;
import com.ia.operation.events.created.OperationCreatedEvent;
import com.ia.operation.events.deleted.OperationDeletedEvent;
import com.ia.operation.events.updated.OperationUpdatedEvent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@NoArgsConstructor
public class OperationAggregate {
    @AggregateIdentifier
    private String id;
    private String description;
    private String accountId;
    private LocalDate operationDate;
    private BigDecimal amount;

    @CommandHandler
    public OperationAggregate(OperationCreationCmd cmd) {
        AggregateLifecycle.apply(OperationCreationCmd.eventFrom(cmd).build());
    }

    @EventSourcingHandler
    public void onOperationUpdated(OperationUpdatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.accountId = event.getAccountId();
        this.operationDate = event.getOperationDate();
        this.amount = event.getAmount();
    }

    @CommandHandler
    public void handleOperationUpdateCmd(OperationUpdateCmd cmd) {
        AggregateLifecycle.apply(OperationUpdateCmd.eventFrom(cmd).build());
    }

    @EventSourcingHandler
    public void onOperationCreated(OperationCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.accountId = event.getAccountId();
        this.operationDate = event.getOperationDate();
        this.amount = event.getAmount();
    }

    @CommandHandler
    public void handleOperationDeletionCmd(OperationDeletionCmd cmd) {
        AggregateLifecycle.apply(OperationDeletedEvent.builder().id(cmd.getId()).build());
        
    }

    @EventSourcingHandler
    public void onOperationDeleted(OperationDeletedEvent event) {
        AggregateLifecycle.markDeleted();
    }
}
