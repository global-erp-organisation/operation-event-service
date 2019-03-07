package com.ia.operation.aggregates;

import java.util.Map;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.operation.commands.creation.UserCreationCmd;
import com.ia.operation.commands.delete.UserDeletionCmd;
import com.ia.operation.commands.update.UserUpdateCmd;
import com.ia.operation.events.created.UserCreatedEvent;
import com.ia.operation.events.deleted.UserDeletedEvent;
import com.ia.operation.events.updated.UserUpdatedEvent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Aggregate(snapshotTriggerDefinition = "snapshotTriggerDefinition")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class UserAggregate {
    @AggregateIdentifier
    private String id;
    private String email;
    private String description;
    private Map<String, Object> details;
    private String companyId;
    private String password;

    @CommandHandler
    public UserAggregate(UserCreationCmd cmd) {
        AggregateLifecycle.apply(UserCreationCmd.eventFrom(cmd).build());
    }

    @EventSourcingHandler
    public void onUserCreated(UserCreatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.details = event.getDetails();
        this.companyId = event.getCompanyId();
        this.email = event.getEmail();
        this.password = event.getPassword();
    }

    @CommandHandler
    public void handleUserUpdateCmd(UserUpdateCmd cmd) {
        AggregateLifecycle.apply(UserUpdateCmd.eventFrom(cmd).build());
    }

    @EventSourcingHandler
    public void onUserUpdated(UserUpdatedEvent event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.details = event.getDetails();
        this.companyId = event.getCompanyId();
        this.email = event.getEmail();
        this.password = event.getPassword();
    }

    @CommandHandler
    public void handleUserDeletionCmd(UserDeletionCmd cmd) {
        AggregateLifecycle.apply(UserDeletedEvent.builder().userId(cmd.getId()).build());
    }

    @EventSourcingHandler
    public void onUserDeleted(UserDeletedEvent event) {
        AggregateLifecycle.markDeleted();
    }
}
