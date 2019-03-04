package com.ia.operation.commands.delete;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.operation.aggregates.AccountCategoryAggregate;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class AccountCategoryDeletionCmd extends CommandValidator<AccountCategoryDeletionCmd> {
    @TargetAggregateIdentifier
    protected String id;

    @Override
    public ValidationResult<AccountCategoryDeletionCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Category identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(id, AccountCategoryAggregate.class).isPresent()) {
                errors.add("The category with id " + id + " doesnt exist");
            }
        }
        return buildResult(errors);
    }
}
