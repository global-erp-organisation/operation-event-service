package com.ia.operation.commands.update;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.aggregates.AccountAggregate;
import com.ia.operation.aggregates.AccountCategoryAggregate;
import com.ia.operation.aggregates.UserAggregate;
import com.ia.operation.enums.AccountType;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.events.updated.AccountUpdatedEvent;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class AccountUpdateCmd extends CommandValidator<AccountUpdateCmd>{
    @TargetAggregateIdentifier
    private String id;
    private String description;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("operation_type")
    private AccountType accountType;
    @JsonProperty("recurring_mode")
    private RecurringMode recurringMode;
    @JsonProperty("default_amount")
    private BigDecimal defaultAmount;
    private String categoryId;
    
    public static AccountUpdateCmdBuilder from(AccountUpdateCmd cmd) {
        return AccountUpdateCmd.builder()
                .defaultAmount(cmd.getDefaultAmount())
                .description(cmd.getDescription())
                .id(cmd.getId())
                .userId(cmd.getUserId())
                .recurringMode(cmd.getRecurringMode())
                .accountType(cmd.getAccountType())
                .categoryId(cmd.getCategoryId());
    }
    
    public static AccountUpdatedEvent.AccountUpdatedEventBuilder of(AccountUpdateCmd cmd) {
        return AccountUpdatedEvent.builder()
                .defaultAmount(cmd.getDefaultAmount())
                .description(cmd.getDescription())
                .id(cmd.getId())
                .userId(cmd.getUserId())
                .recurringMode(cmd.getRecurringMode())
                .accountType(cmd.getAccountType())
                .categoryId(cmd.getCategoryId());
    }

    @Override
    public ValidationResult<AccountUpdateCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(categoryId)) {
            errors.add("Category identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(categoryId, AccountCategoryAggregate.class).isPresent()) {
                errors.add("The category with id " + categoryId + " doesnt exist");
            }
        }
        if(StringUtils.isEmpty(id)) {
            errors.add("The operation identifier shouldnt be null nor empty.");
        }else {
            if (!util.aggregateGet(id, AccountAggregate.class).isPresent()) {
                errors.add("The operation with id " + id + " doesnt exists");
            }
        }
        if(StringUtils.isEmpty(userId)) {
            errors.add("The user identifier shouldnt be null or empty");
        }else {
            if(!util.aggregateGet(userId, UserAggregate.class).isPresent()) {
                errors.add("The user with id "+ userId + "doesnt exist");
            }
        }
        if(StringUtils.isEmpty(description)) {
            errors.add("The operation description shouldnt be null or empty");
        }
        if(!AccountType.check(accountType)) {
            errors.add("The input operation type doesnt match with the available domain. Here is the domain value: " + AccountType.values());
        }
        if(!RecurringMode.check(recurringMode)) {
            errors.add("The input recurring mode doesnt match with the available domain. Here is the domain value: " + RecurringMode.values());
        }
        return buildResult(errors);
    }
}
