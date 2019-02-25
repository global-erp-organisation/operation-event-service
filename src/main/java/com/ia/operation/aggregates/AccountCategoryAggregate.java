package com.ia.operation.aggregates;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.operation.commands.creation.AccountCategoryCreationCmd;
import com.ia.operation.commands.delete.AccountCategoryDeletionCmd;
import com.ia.operation.commands.update.AccountCategoryUpdateCmd;
import com.ia.operation.events.created.AccountCategoryCreatedEvent;
import com.ia.operation.events.deleted.AccountCategoryDeletedEvent;
import com.ia.operation.events.updated.AccountCategoryUpdatedEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@NoArgsConstructor
public class AccountCategoryAggregate {
    @AggregateIdentifier
    private String id;
    private String description;

    @CommandHandler
    public AccountCategoryAggregate(AccountCategoryCreationCmd cmd) {
        AggregateLifecycle.apply(AccountCategoryCreationCmd.from(cmd).build());
    }

    @EventSourcingHandler
    public void onOperationCategoryCreated(AccountCategoryCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
    }

    @CommandHandler
    public void handleOperationCategoryUpdateCmd(AccountCategoryUpdateCmd cmd) {
        AggregateLifecycle.apply(AccountCategoryUpdateCmd.from(cmd).build());
    }

    @EventSourcingHandler
    public void onOperationCategoryUpdated(AccountCategoryUpdatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
    }

    @CommandHandler
    public void handleOperationCategoryDeleteCmd(AccountCategoryDeletionCmd cmd) {
        AggregateLifecycle.apply(AccountCategoryDeletionCmd.builder().categoryId(cmd.getCategoryId()).build());
        AggregateLifecycle.markDeleted();
    }

    @EventSourcingHandler
    public void onOperationCategoryDeleted(AccountCategoryDeletedEvent event) {
        this.id = event.getCategoryId();
    }
}