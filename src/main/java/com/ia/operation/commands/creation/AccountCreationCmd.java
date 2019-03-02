package com.ia.operation.commands.creation;

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
import com.ia.operation.events.created.AccountCreatedEvent;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class AccountCreationCmd extends CommandValidator<AccountCreationCmd> {
    @TargetAggregateIdentifier
    private String id;
    private String description;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("operation_type")
    @Builder.Default
    private AccountType accountType = AccountType.UNDEFINED;
    @JsonProperty("recurring_mode")
    @Builder.Default
    private RecurringMode recurringMode = RecurringMode.NONE;
    @JsonProperty("default_amount")
    private BigDecimal defaultAmount;
    private String categoryId;
    
    public static AccountCreationCmdBuilder cmdFrom(AccountCreationCmd cmd) {
        return AccountCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .userId(cmd.getUserId())
                .recurringMode(cmd.getRecurringMode())
                .defaultAmount(cmd.getDefaultAmount())
                .accountType(cmd.getAccountType())
                .categoryId(cmd.getCategoryId());
    }
    
    
    public static AccountCreatedEvent enventFrom(AccountCreationCmd cmd) {
        return AccountCreatedEvent.builder()
                .id(cmd.getId())
                .userId(cmd.getUserId())
                .description(cmd.getDescription())
                .recurringMode(cmd.getRecurringMode())
                .accountType(cmd.getAccountType())
                .defaultAmount(cmd.getDefaultAmount())
                .categoryId(cmd.getCategoryId())
                .build();
    }
    
    @Override
    public ValidationResult<AccountCreationCmd> validate(AggregateUtil util) {
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
            if (util.aggregateGet(id, AccountAggregate.class).isPresent()) {
                errors.add("The operation with id " + id + " already exists");
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
