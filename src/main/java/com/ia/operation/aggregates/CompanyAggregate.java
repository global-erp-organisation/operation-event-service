package com.ia.operation.aggregates;

import java.util.Map;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.operation.commands.creation.CompanyCreationCmd;
import com.ia.operation.commands.delete.CompanyDeleteCmd;
import com.ia.operation.commands.update.CompanyUpdateCmd;
import com.ia.operation.events.created.CompanyCreatedEvent;
import com.ia.operation.events.deleted.CompanyDeletedEvent;
import com.ia.operation.events.updated.CompanyUpdatedEvent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CompanyAggregate {
    @AggregateIdentifier
    private String id;
    private String description;
    private Map<String, Object> details;

    @CommandHandler
    public CompanyAggregate(CompanyCreationCmd cmd) {
        AggregateLifecycle.apply(CompanyCreationCmd.from(cmd).build());
    }

    @EventSourcingHandler
    public void onCompanyCreated(CompanyCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.details = event.getDetails();
    }

    @CommandHandler
    public void handleCompanyUpdateCmd(CompanyUpdateCmd cmd) {
        AggregateLifecycle.apply(CompanyUpdateCmd.from(cmd).build());
    }

    @EventSourcingHandler
    public void onCompanyUpdated(CompanyUpdatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.details = event.getDetails();
    }

    @CommandHandler
    public void handleCompanyDeletionCmd(CompanyDeleteCmd cmd) {
        AggregateLifecycle.apply(CompanyDeletedEvent.builder().companyId(cmd.getId()).build());
        AggregateLifecycle.markDeleted();
    }

    @EventSourcingHandler
    public void onCompanyDeleted(CompanyDeletedEvent event) {
        this.id = event.getCompanyId();
    }
}
