package com.ia.operation.commands.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.operation.aggregates.CompanyAggregate;
import com.ia.operation.events.updated.CompanyUpdatedEvent;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class CompanyUpdateCmd extends CommandValidator<CompanyUpdateCmd> {
    @TargetAggregateIdentifier
    protected String id;

    private String description;
    private Map<String, Object> details;

    public static CompanyUpdateCmdBuilder cmdFrom(CompanyUpdateCmd cmd) {
        /*@formatter:off*/
        return CompanyUpdateCmd.builder()
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .id(cmd.getId());
        /*@formatter:on*/
    }

    public static CompanyUpdatedEvent.CompanyUpdatedEventBuilder eventFrom(CompanyUpdateCmd cmd) {
        /*@formatter:off*/
        return CompanyUpdatedEvent.builder()
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .id(cmd.getId());
        /*@formatter:on*/
    }

    @Override
    public ValidationResult<CompanyUpdateCmd> validate(AggregateHelper util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Company identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(id, CompanyAggregate.class).isPresent()) {
                errors.add("The company with id " + id + " doesnt exist");
            }
        }
        if (StringUtil.isNullOrEmpty(description)) {
            errors.add("Company description shouldn't be null or empty");
        }
        return buildResult(errors);
    }
}
