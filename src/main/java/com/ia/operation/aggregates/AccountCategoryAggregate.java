package com.ia.operation.aggregates;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;
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
        apply(AccountCategoryCreationCmd.eventFrom(cmd).build());
    }

    @EventSourcingHandler
    public void onOperationCategoryCreated(AccountCategoryCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
    }

    @CommandHandler
    public void handleOperationCategoryUpdateCmd(AccountCategoryUpdateCmd cmd) {
        apply(AccountCategoryUpdateCmd.eventFrom(cmd).build());
    }

    @EventSourcingHandler
    public void onOperationCategoryUpdated(AccountCategoryUpdatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
    }

    @CommandHandler
    public void handleOperationCategoryDeleteCmd(AccountCategoryDeletionCmd cmd) {
        apply(AccountCategoryDeletionCmd.builder().id(cmd.getId()).build());
    }

    @EventSourcingHandler
    public void onOperationCategoryDeleted(AccountCategoryDeletedEvent event) {
        markDeleted();
    }
}
