package com.ia.operation.commands.update;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.operation.aggregates.AccountCategoryAggregate;
import com.ia.operation.events.updated.AccountCategoryUpdatedEvent;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class AccountCategoryUpdateCmd extends CommandValidator<AccountCategoryUpdateCmd> {
    @TargetAggregateIdentifier
    protected String id;

    private String description;

    public static AccountCategoryUpdatedEvent.AccountCategoryUpdatedEventBuilder eventFrom(AccountCategoryUpdateCmd cmd) {
        /*@formatter:off*/
        return AccountCategoryUpdatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription());
        /*@formatter:on*/
    }

    public static AccountCategoryUpdateCmdBuilder cmdFrom(AccountCategoryUpdateCmd cmd) {
        /*@formatter:off*/
        return AccountCategoryUpdateCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription());
        /*@formatter:on*/
    }

    @Override
    public ValidationResult<AccountCategoryUpdateCmd> validate(AggregateHelper util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Category identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(id, AccountCategoryAggregate.class).isPresent()) {
                errors.add("The category with id " + id + " doesnt exist");
            }
        }
        if (StringUtil.isNullOrEmpty(description)) {
            errors.add("Category description shouldn't be null or empty");
        }
        return buildResult(errors);
    }
}
