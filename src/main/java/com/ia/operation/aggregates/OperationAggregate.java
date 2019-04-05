package com.ia.operation.aggregates;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.ia.operation.commands.creation.OperationCreationCmd;
import com.ia.operation.commands.delete.OperationDeletionCmd;
import com.ia.operation.commands.update.AccountUpdateCmd;
import com.ia.operation.commands.update.OperationUpdateCmd;
import com.ia.operation.enums.AccountType;
import com.ia.operation.enums.OperationType;
import com.ia.operation.events.created.OperationCreatedEvent;
import com.ia.operation.events.deleted.OperationDeletedEvent;
import com.ia.operation.events.updated.OperationUpdatedEvent;
import com.ia.operation.util.AggregateUtil;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

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
    private OperationType operationType;

    @CommandHandler
    public OperationAggregate(OperationCreationCmd cmd, AggregateUtil util, CommandGateway gateway) {
        AggregateLifecycle.apply(OperationCreationCmd.eventFrom(cmd).build()).andThen(() -> {
            final UpdateParams params = UpdateParams.builder().eventType(EventType.A).accountId(cmd.getAccountId()).amount(cmd.getAmount())
                    .operationType(cmd.getOperationType()).build();
            accountUpdate(params, util, gateway);
        });
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
    public void handleOperationUpdateCmd(OperationUpdateCmd cmd, AggregateUtil util, CommandGateway gateway) {
        AggregateLifecycle.apply(OperationUpdateCmd.eventFrom(cmd).build()).andThen(() -> {
            final UpdateParams params = UpdateParams.builder().eventType(EventType.U).accountId(cmd.getAccountId()).amount(cmd.getAmount())
                    .operationType(cmd.getOperationType()).build();
            accountUpdate(params, util, gateway);
        });
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
    public void handleOperationDeletionCmd(OperationDeletionCmd cmd, AggregateUtil util, CommandGateway gateway) {
        final UpdateParams params =
                UpdateParams.builder().eventType(EventType.D).accountId(getAccountId()).amount(getAmount()).operationType(getOperationType()).build();
        AggregateLifecycle.apply(OperationDeletedEvent.builder().id(cmd.getId()).build()).andThen(() -> accountUpdate(params, util, gateway));
    }

    @EventSourcingHandler
    public void onOperationDeleted(OperationDeletedEvent event) {
        AggregateLifecycle.markDeleted();
    }

    private void accountUpdate(UpdateParams params, AggregateUtil util, CommandGateway gateway) {
        final AccountAggregate account = util.aggregateGet(params.getAccountId(), AccountAggregate.class).get();
        if (AccountType.BANKING.equals(account.getAccountType())) {
            final BigDecimal current = account.getBalance() == null ? BigDecimal.ZERO : account.getBalance();
            final AccountUpdateCmd.AccountUpdateCmdBuilder builder = AccountUpdateCmd.cmdFrom(account);

            if (EventType.D.equals(params.getEventType())) {
                if (OperationType.EXPENSE.equals(params.getOperationType())) {
                    builder.balance(current.add(params.getAmount()));
                } else if (OperationType.REVENUE.equals(params.getOperationType())) {
                    builder.balance(current.subtract(params.getAmount()));
                }
            } else {
                if (OperationType.EXPENSE.equals(params.getOperationType())) {
                    builder.balance(current.subtract(params.getAmount()));
                } else if (OperationType.REVENUE.equals(params.getOperationType())) {
                    builder.balance(current.add(params.getAmount()));
                }
            }
            gateway.send(builder.build());
        }
    }

    @Value
    @Builder
    static class UpdateParams {
        private String accountId;
        private OperationType operationType;
        private BigDecimal amount;
        private EventType eventType;
    }

    enum EventType {
       A,U,D;
    }
}
