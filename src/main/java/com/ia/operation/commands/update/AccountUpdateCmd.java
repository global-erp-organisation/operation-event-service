package com.ia.operation.commands.update;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.aggregates.AccountAggregate;
import com.ia.operation.aggregates.AccountCategoryAggregate;
import com.ia.operation.aggregates.UserAggregate;
import com.ia.operation.enums.AccountType;
import com.ia.operation.enums.OperationType;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.events.updated.AccountUpdatedEvent;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.validator.CommandValidator;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class AccountUpdateCmd extends CommandValidator<AccountUpdateCmd> {
    @TargetAggregateIdentifier
    protected String id;

    private String description;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("recurring_mode")
    private RecurringMode recurringMode;
    @JsonProperty("default_amount")
    private BigDecimal defaultAmount;
    private String categoryId;
    private BigDecimal balance;
    @JsonProperty("account_type")
    private AccountType accountType;
    @JsonProperty("default_operation_type")
    private OperationType defaultOperationType;

    public static AccountUpdateCmdBuilder cmdFrom(AccountUpdateCmd cmd) {
        /*@formatter:off*/
        return AccountUpdateCmd.builder()
                .defaultAmount(cmd.getDefaultAmount())
                .description(cmd.getDescription())
                .id(cmd.getId())
                .userId(cmd.getUserId())
                .recurringMode(cmd.getRecurringMode())
                .categoryId(cmd.getCategoryId())
                .balance(cmd.getBalance())
                .accountType(cmd.getAccountType())
                .defaultOperationType(cmd.getDefaultOperationType());
        /*@formatter:on*/
    }

    public static AccountUpdateCmdBuilder cmdFrom(AccountAggregate cmd) {
        /*@formatter:off*/
        return AccountUpdateCmd.builder()
                .defaultAmount(cmd.getDefaultAmount())
                .description(cmd.getDescription())
                .id(cmd.getId())
                .userId(cmd.getUserId())
                .recurringMode(cmd.getRecurringMode())
                .categoryId(cmd.getCategoryId())
                .balance(cmd.getBalance())
                .accountType(cmd.getAccountType())
                .defaultOperationType(cmd.getDefaultOperationType());
        /*@formatter:on*/
    }

    public static AccountUpdatedEvent.AccountUpdatedEventBuilder eventFrom(AccountUpdateCmd cmd) {
        /*@formatter:off*/
        return AccountUpdatedEvent.builder()
                .defaultAmount(cmd.getDefaultAmount())
                .description(cmd.getDescription())
                .id(cmd.getId())
                .userId(cmd.getUserId())
                .recurringMode(cmd.getRecurringMode())
                .categoryId(cmd.getCategoryId())
                .balance(cmd.getBalance())
                .accountType(cmd.getAccountType())
                .defaultOperationType(cmd.getDefaultOperationType());
        /*@formatter:on*/
    }

    @Override
    public ValidationResult<AccountUpdateCmd> validate(AggregateHelper util) {
        final List<String> errors = new ArrayList<>();

        if (StringUtils.isEmpty(id)) {
            errors.add("The operation identifier shouldnt be null nor empty.");
        } else {
            final Optional<AccountAggregate> ac = util.aggregateGet(id, AccountAggregate.class);
            if (!ac.isPresent()) {
                errors.add("The operation with id " + id + " doesnt exists");
            } else {
                setCategoryId(categoryId == null ? ac.get().getCategoryId() : categoryId);
                setUserId(userId == null ? ac.get().getUserId() : userId);
            }
        }

        if (StringUtils.isEmpty(categoryId)) {
            errors.add("Category identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(categoryId, AccountCategoryAggregate.class).isPresent()) {
                errors.add("The category with id " + categoryId + " doesnt exist");
            }
        }
        if (StringUtils.isEmpty(userId)) {
            errors.add("The user identifier shouldnt be null or empty");
        } else {
            if (!util.aggregateGet(userId, UserAggregate.class).isPresent()) {
                errors.add("The user with id " + userId + "doesnt exist");
            }
        }
        if (StringUtils.isEmpty(description)) {
            errors.add("The operation description shouldnt be null or empty");
        }
        if (!RecurringMode.check(recurringMode)) {
            errors.add("The input recurring mode doesnt match with the available domain. Here is the domain value: " + RecurringMode.values());
        }
        if (!AccountType.check(accountType)) {
            final List<String> values = Stream.of(AccountType.values()).map(a -> a.name()).collect(Collectors.toList());
            errors.add("The input account type doesnt match with the available domain. Here is the domain value: " + String.join(",", values));
        }
        if (balance.doubleValue() < 0.0) {
            errors.add("The input balance shouldnt be less than zero.");
        }
        return buildResult(errors);
    }
}
