package com.ia.operation.aggregates;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.operation.commands.creation.ProjectionCreationCmd;
import com.ia.operation.commands.delete.ProjectionDeletionCmd;
import com.ia.operation.events.created.ProjectionCreatedEvent;
import com.ia.operation.events.deleted.ProjectionDeletedEvent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@NoArgsConstructor
public class ProjectionAggregate {
    @AggregateIdentifier
    private String id;
    private String accountId;
    private BigDecimal amount;
    private String periodId;

    @CommandHandler
    public ProjectionAggregate(ProjectionCreationCmd cmd) {
        AggregateLifecycle.apply(ProjectionCreationCmd.of(cmd));
    }

    @EventSourcingHandler
    public void onProjectionCreated(ProjectionCreatedEvent event) {
        this.id = event.getId();
        this.accountId = event.getAccountId();
        this.amount = event.getAmount();
        this.periodId = event.getPeriodId();
    }

    @CommandHandler
    public void handleProjectionDeletionCmd(ProjectionDeletionCmd cmd) {
        AggregateLifecycle.apply(ProjectionDeletedEvent.builder().id(cmd.getId()).build());
        AggregateLifecycle.markDeleted();
    }

    @EventSourcingHandler
    public void onProjectionDeleted(ProjectionDeletedEvent event) {
        this.id = event.getId();
    }
}
