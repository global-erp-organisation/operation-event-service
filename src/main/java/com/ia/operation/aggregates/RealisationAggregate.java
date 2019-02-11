package com.ia.operation.aggregates;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.operation.commands.creation.RealisationCreationCmd;
import com.ia.operation.commands.delete.RealisationDeleteCmd;
import com.ia.operation.commands.update.RealisationUpdateCmd;
import com.ia.operation.events.created.RealisationCreatedEvent;
import com.ia.operation.events.deleted.RealisationDeletedEvent;
import com.ia.operation.events.updated.RealisationUpdatedEvent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@NoArgsConstructor
public class RealisationAggregate {
    @AggregateIdentifier
    private String id;
    private String description;
    private String operationId;
    private LocalDate operationDate;
    private BigDecimal amount;

    @CommandHandler
    public RealisationAggregate(RealisationCreationCmd cmd) {
        AggregateLifecycle.apply(RealisationCreationCmd.of(cmd).build());
    }

    @EventSourcingHandler
    public void onRealisationUpdated(RealisationUpdatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.operationId = event.getOperationId();
        this.operationDate = event.getOperationDate();
        this.amount = event.getAmount();
    }

    @CommandHandler
    public void handleRealisationUpdateCmd(RealisationUpdateCmd cmd) {
        AggregateLifecycle.apply(RealisationUpdateCmd.of(cmd).build());
    }

    @EventSourcingHandler
    public void onRealisationCreated(RealisationCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.operationId = event.getOperationId();
        this.operationDate = event.getOperationDate();
        this.amount = event.getAmount();
    }

    @CommandHandler
    public void handleRealisationDeletionCmd(RealisationDeleteCmd cmd) {
        AggregateLifecycle.apply(RealisationDeletedEvent.builder().id(cmd.getId()).build());
        AggregateLifecycle.markDeleted();
    }

    @EventSourcingHandler
    public void onRealisationDeleted(RealisationDeletedEvent event) {
        this.id = event.getId();
    }
}
