package com.ia.operation.commands.delete;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.operation.aggregates.AccountAggregate;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.validator.CommandValidator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class AccountDeletionCmd extends CommandValidator<AccountDeletionCmd> {
    @TargetAggregateIdentifier
    protected String id;

    @Override
    public ValidationResult<AccountDeletionCmd> validate(AggregateHelper util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Account identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(id, AccountAggregate.class).isPresent()) {
                errors.add("The operaration with id " + id + " doesnt exist");
            }
        }
        return buildResult(errors);
    }
}
