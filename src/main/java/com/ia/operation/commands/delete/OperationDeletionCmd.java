package com.ia.operation.commands.delete;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.operation.aggregates.OperationAggregate;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class OperationDeletionCmd extends CommandValidator<OperationDeletionCmd> {
    @TargetAggregateIdentifier
    protected String id;

    @Override
    public ValidationResult<OperationDeletionCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Operation identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(id, OperationAggregate.class).isPresent()) {
                errors.add("The Operation with id " + id + " doesnt exist");
            }
        }
        return buildResult(errors);
    }
}
