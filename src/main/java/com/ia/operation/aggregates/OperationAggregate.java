package com.ia.operation.aggregates;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.operation.commands.creation.OperationCreationCmd;
import com.ia.operation.commands.delete.OperationDeleteCmd;
import com.ia.operation.commands.update.OperationUpdateCmd;
import com.ia.operation.enums.OperationType;
import com.ia.operation.enums.RecurringMode;
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
    private OperationType operationType;
    private String userId;
    private RecurringMode recurringMode;
    private BigDecimal defaultAmount;
    private String categoryId;

    @CommandHandler
    public OperationAggregate(OperationCreationCmd cmd) {
        AggregateLifecycle.apply(OperationCreationCmd.of(cmd));
    }

    @EventSourcingHandler
    public void onOperationCreated(OperationCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.operationType = event.getOperationType();
        this.userId = event.getUserId();
        this.recurringMode = event.getRecurringMode();
        this.defaultAmount = event.getDefaultAmount();
        this.categoryId = event.getCategoryId();
    }

    @CommandHandler
    public void handleOperationUpdateCmd(OperationUpdateCmd cmd) {
        AggregateLifecycle.apply(OperationUpdateCmd.of(cmd).build());
    }

    @EventSourcingHandler
    public void onOperationCreated(OperationUpdatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.operationType = event.getOperationType();
        this.userId = event.getUserId();
        this.recurringMode = event.getRecurringMode();
        this.defaultAmount = event.getDefaultAmount();
        this.categoryId = event.getCategoryId();
    }

    @CommandHandler
    public void handleOperationDeletionCmd(OperationDeleteCmd cmd) {
        AggregateLifecycle.apply(OperationDeletedEvent.builder().id(cmd.getId()).build());
        AggregateLifecycle.markDeleted();
    }

    @EventSourcingHandler
    public void onOperationDeleted(OperationDeletedEvent event) {
        this.id = event.getId();
    }
}
