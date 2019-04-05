package com.ia.operation.commands.update;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.aggregates.AccountAggregate;
import com.ia.operation.aggregates.OperationAggregate;
import com.ia.operation.enums.OperationType;
import com.ia.operation.events.updated.OperationUpdatedEvent;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class OperationUpdateCmd extends CommandValidator<OperationUpdateCmd> {
    @TargetAggregateIdentifier
    protected String id;
    private String description;
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("operation_date")
    private LocalDate operationDate;
    private BigDecimal amount;
    @JsonProperty("operation_type")
    private OperationType operationType;

    public static OperationUpdateCmdBuilder cmdFrom(OperationUpdateCmd cmd) {
        return OperationUpdateCmd.builder().id(cmd.getId()).description(cmd.getDescription()).operationDate(cmd.getOperationDate())
                .accountId(cmd.getAccountId()).amount(cmd.getAmount())
                .operationType(cmd.getOperationType());
    }

    public static OperationUpdatedEvent.OperationUpdatedEventBuilder eventFrom(OperationUpdateCmd cmd) {
        return OperationUpdatedEvent.builder().id(cmd.getId()).description(cmd.getDescription()).operationDate(cmd.getOperationDate())
                .accountId(cmd.getAccountId()).amount(cmd.getAmount())
                .operationType(cmd.getOperationType());
    }

    @Override
    public ValidationResult<OperationUpdateCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();

        if (StringUtils.isEmpty(id)) {
            errors.add("The operation identifier shouldnt be null nor empty.");
        } else {
            final Optional<OperationAggregate> op = util.aggregateGet(id, OperationAggregate.class);
            if (op.isPresent()) {
                setAccountId(accountId == null ? op.get().getAccountId() : accountId);
            }
        }

        if (!StringUtils.isEmpty(accountId)) {
            if (!util.aggregateGet(accountId, AccountAggregate.class).isPresent()) {
                errors.add("The account with id " + accountId + " doesnt exist");
            }
        }
        if (StringUtils.isEmpty(id)) {
            errors.add("The operation identifier shouldnt be null nor empty.");
        } else {
            if (!util.aggregateGet(id, OperationAggregate.class).isPresent()) {
                errors.add("The operation with id " + id + " doesnt exists");
            }
        }
        if (amount.doubleValue() <= 0.0) {
            errors.add("The operation amount should be greater than zero.");
        }
        if (!OperationType.check(operationType)) {
            final List<String> values = Stream.of(OperationType.values()).map(a -> a.name()).collect(Collectors.toList());
            errors.add("The input account type doesnt match with the available domain. Here is the domain value: " + String.join(",", values));
        }

        return buildResult(errors);
    }
}
