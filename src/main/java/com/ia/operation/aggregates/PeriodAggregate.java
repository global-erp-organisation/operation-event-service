package com.ia.operation.aggregates;

import java.time.LocalDate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.operation.commands.creation.PeriodCreationCmd;
import com.ia.operation.commands.delete.PeriodDeletionCmd;
import com.ia.operation.events.created.PeriodCreatedEvent;
import com.ia.operation.events.deleted.PeriodDeletedEvent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@NoArgsConstructor
public class PeriodAggregate {
    @AggregateIdentifier
    private String id;
    private String year;
    private LocalDate start;
    private LocalDate end;
    private String description;
    private Boolean close;

    @CommandHandler
    public PeriodAggregate(PeriodCreationCmd cmd) {
        apply(cmd.getEvent());
    }

    @EventSourcingHandler
    public void onPeriodCreated(PeriodCreatedEvent event) {
        this.id = event.getId();
        this.year = event.getYear();
        this.description = event.getDescription();
        this.close = event.getClose();
    }

    @CommandHandler
    public void handlePeriodDeleteCmd(PeriodDeletionCmd cmd) {
        if (getClose()) {
            throw new IllegalStateException("Deleting closed periods is prohibited");
        }
        apply(PeriodDeletedEvent.builder().id(cmd.getId()).build());
    }

    @EventSourcingHandler
    public void onPeriodDeleted(PeriodDeletedEvent event) {
        markDeleted();
    }
}
