package com.ia.operation.commands.delete;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.operation.aggregates.ProjectionAggregate;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class ProjectionDeletionCmd extends CommandValidator<ProjectionDeletionCmd> {
    @TargetAggregateIdentifier
    private String id;

    @Override
    public ValidationResult<ProjectionDeletionCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Projection identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(id, ProjectionAggregate.class).isPresent()) {
                errors.add("The projection with id " + id + " doesnt exist");
            }
        }
        return buildResult(errors);

    }
}
