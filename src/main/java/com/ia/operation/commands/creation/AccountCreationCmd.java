package com.ia.operation.commands.creation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import com.ia.operation.events.created.AccountCreatedEvent;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class AccountCreationCmd extends CommandValidator<AccountCreationCmd> {

    @TargetAggregateIdentifier
    protected String id;

    private String description;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("default_amount")
    private BigDecimal defaultAmount;
    @JsonProperty("category_id")
    private String categoryId;
    @JsonProperty("recurring_mode")
    @Builder.Default
    private RecurringMode recurringMode = RecurringMode.NONE;
    private BigDecimal balance;
    @JsonProperty("account_type")
    @Builder.Default
    private AccountType accountType = AccountType.OTHER;
    @Builder.Default
    @JsonProperty("default_operation_type")
    private OperationType defaultOperationType = OperationType.UNDEFINED;

    public static AccountCreationCmdBuilder cmdFrom(AccountCreationCmd cmd) {
        return AccountCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .userId(cmd.getUserId())
                .recurringMode(cmd.getRecurringMode())
                .defaultAmount(cmd.getDefaultAmount())
                .categoryId(cmd.getCategoryId())
                .balance(cmd.getBalance())
                .accountType(cmd.getAccountType())
                .defaultOperationType(cmd.getDefaultOperationType());
    }

    public static AccountCreatedEvent.AccountCreatedEventBuilder enventFrom(AccountCreationCmd cmd) {
        return AccountCreatedEvent.builder()
                .id(cmd.getId())
                .userId(cmd.getUserId())
                .description(cmd.getDescription())
                .recurringMode(cmd.getRecurringMode())
                .defaultAmount(cmd.getDefaultAmount())
                .balance(cmd.getBalance())
                .categoryId(cmd.getCategoryId())
                .accountType(cmd.getAccountType())
                .defaultOperationType(cmd.getDefaultOperationType());
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
        if (StringUtils.isEmpty(id)) {
            errors.add("The account identifier shouldnt be null nor empty.");
        } else {
            if (util.aggregateGet(id, AccountAggregate.class).isPresent()) {
                errors.add("The account with id " + id + " already exists");
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
            errors.add("The account description shouldnt be null or empty");
        }
        if (!RecurringMode.check(recurringMode)) {
            final List<String> values = Stream.of(RecurringMode.values()).map(a -> a.name()).collect(Collectors.toList());
            errors.add("The input recurring mode doesnt match with the available domain. Here is the domain value: " + String.join(",", values));
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
