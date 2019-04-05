package com.ia.operation.commands.creation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.aggregates.AccountAggregate;
import com.ia.operation.aggregates.OperationAggregate;
import com.ia.operation.enums.OperationType;
import com.ia.operation.events.created.OperationCreatedEvent;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class OperationCreationCmd  extends CommandValidator<OperationCreationCmd>{
    @TargetAggregateIdentifier
    protected String id;

    private String description;
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("operation_date")
    private LocalDate operationDate;
    private BigDecimal amount;
    @Builder.Default
    @JsonProperty("operation_type")
    private OperationType operationType = OperationType.UNDEFINED;
    
    public static OperationCreationCmdBuilder cmdFrom(OperationCreationCmd cmd) {
        return OperationCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .operationDate(cmd.getOperationDate())
                .accountId(cmd.getAccountId())
                .amount(cmd.getAmount())
                .operationType(cmd.getOperationType());
    }
    
    public static OperationCreatedEvent.OperationCreatedEventBuilder eventFrom(OperationCreationCmd cmd) {
        return OperationCreatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .operationDate(cmd.getOperationDate())
                .accountId(cmd.getAccountId())
                .amount(cmd.getAmount())
                .operationType(cmd.getOperationType());
    }

    @Override
    public ValidationResult<OperationCreationCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(accountId)) {
            errors.add("Account identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(accountId, AccountAggregate.class).isPresent()) {
                errors.add("The account with id " + accountId + " doesnt exist");
            }
        }
        if(StringUtils.isEmpty(id)) {
            errors.add("The operation identifier shouldnt be null nor empty.");
        }else {
            if (util.aggregateGet(id, OperationAggregate.class).isPresent()) {
                errors.add("The operation with id " + id + " already exists");
            }
        }
        if(StringUtils.isEmpty(description)) {
            errors.add("The operation description shouldnt be null or empty");
        }
        if(amount.doubleValue()<=0.0) {
            errors.add("The operation amount should be greater than zero.");
        }
        if (!OperationType.check(operationType)) {
            final List<String> values = Stream.of(OperationType.values()).map(a -> a.name()).collect(Collectors.toList());
            errors.add("The input account type doesnt match with the available domain. Here is the domain value: " + String.join(",", values));
        }

        return buildResult(errors);
    }
}
