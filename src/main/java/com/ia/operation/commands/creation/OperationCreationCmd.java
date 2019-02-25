package com.ia.operation.commands.creation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.aggregates.AccountAggregate;
import com.ia.operation.aggregates.OperationAggregate;
import com.ia.operation.events.created.RealisationCreatedEvent;
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
    private String id;
    private String description;
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("operation_date")
    private LocalDate operationDate;
    private BigDecimal amount;
    
    public static OperationCreationCmdBuilder from(OperationCreationCmd cmd) {
        return OperationCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .operationDate(cmd.getOperationDate())
                .accountId(cmd.getAccountId())
                .amount(cmd.getAmount());
    }
    
    public static RealisationCreatedEvent.RealisationCreatedEventBuilder of(OperationCreationCmd cmd) {
        return RealisationCreatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .operationDate(cmd.getOperationDate())
                .accountId(cmd.getAccountId())
                .amount(cmd.getAmount());
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
        return buildResult(errors);
    }
}
