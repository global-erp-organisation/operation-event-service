package com.ia.operation.helper;

import java.math.BigDecimal;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;

import com.ia.operation.aggregates.AccountAggregate;
import com.ia.operation.commands.update.AccountUpdateCmd;
import com.ia.operation.enums.AccountType;
import com.ia.operation.enums.OperationType;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Component
public class AccountUpdater {

    private final AggregateHelper util;
    private final CommandGateway gateway;

    public void update(UpdateParams params) {
        final AccountAggregate account = util.aggregateGet(params.getAccountId(), AccountAggregate.class).get();
        if (AccountType.BANKING.equals(account.getAccountType())) {
            final BigDecimal current = account.getBalance() == null ? BigDecimal.ZERO : account.getBalance();
            final AccountUpdateCmd.AccountUpdateCmdBuilder builder = AccountUpdateCmd.cmdFrom(account);

            switch (params.getEventType()) {
                case A:
                    if (OperationType.EXPENSE.equals(params.getOperationType())) {
                        builder.balance(current.subtract(params.getAmount()));
                    } else if (OperationType.REVENUE.equals(params.getOperationType())) {
                        builder.balance(current.add(params.getAmount()));
                    }
                    break;
                case U:
                    if (OperationType.EXPENSE.equals(params.getOperationType())) {
                        builder.balance(current.subtract(params.getOldAmount()).subtract(params.getAmount()));
                    } else if (OperationType.REVENUE.equals(params.getOperationType())) {
                        builder.balance(current.subtract(params.getOldAmount()).add(params.getAmount()));
                    }
                    break;
                case D:
                    if (OperationType.EXPENSE.equals(params.getOperationType())) {
                        builder.balance(current.add(params.getAmount()));
                    } else if (OperationType.REVENUE.equals(params.getOperationType())) {
                        builder.balance(current.subtract(params.getAmount()));
                    }
                    break;
                default:
                    break;
            }
            gateway.send(builder.build());
        }
    }

    @Value
    @Builder
    public static class UpdateParams {
        private String accountId;
        private OperationType operationType;
        private BigDecimal amount;
        private BigDecimal oldAmount;
        private EventType eventType;
    }

    public enum EventType {
        A, // A new Operation creation command have been handled.
        U, // A new Operation update command have been handled.
        D; // A new Operation deletion command have been handled.
    }
}
