package com.ia.operation.aggregates;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.operation.commands.creation.OperationCategoryCreationCmd;
import com.ia.operation.commands.delete.OperationCategoryDeletionCmd;
import com.ia.operation.commands.update.OperationCategoryUpdateCmd;
import com.ia.operation.events.created.OperationCategoryCreatedEvent;
import com.ia.operation.events.deleted.OperationCategoryDeletedEvent;
import com.ia.operation.events.updated.OperationCategoryUpdatedEvent;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Aggregate
public class OperationCategoryAggregate {
    private String id;
    private String description;

    @CommandHandler
    public OperationCategoryAggregate(OperationCategoryCreationCmd cmd) {
        AggregateLifecycle.apply(OperationCategoryCreationCmd.from(cmd).build());
    }

    @EventSourcingHandler
    public void onOperationCategoryCreated(OperationCategoryCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
    }

    @CommandHandler
    public void handleOperationCategoryUpdateCmd(OperationCategoryUpdateCmd cmd) {
        AggregateLifecycle.apply(OperationCategoryUpdateCmd.from(cmd).build());
    }

    @EventSourcingHandler
    public void onOperationCategoryUpdated(OperationCategoryUpdatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
    }

    @CommandHandler
    public void handleOperationCategoryDeleteCmd(OperationCategoryDeletionCmd cmd) {
        AggregateLifecycle.apply(OperationCategoryDeletionCmd.builder().categoryId(cmd.getCategoryId()).build());
        AggregateLifecycle.markDeleted();
    }

    @EventSourcingHandler
    public void onOperationCategoryDeleted(OperationCategoryDeletedEvent event) {
        this.id = event.getCategoryId();
    }
}
