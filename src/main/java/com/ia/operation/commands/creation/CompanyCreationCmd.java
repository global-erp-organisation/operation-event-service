package com.ia.operation.commands.creation;

import com.ia.operation.aggregates.CompanyAggregate;
import com.ia.operation.commands.update.CompanyUpdateCmd;
import com.ia.operation.events.created.CompanyCreatedEvent;
import com.ia.operation.events.created.CompanyCreatedEvent.CompanyCreatedEventBuilder;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.validator.CommandValidator;
import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class CompanyCreationCmd extends CommandValidator<CompanyCreationCmd> {

    protected String id;
    String description;
    Map<String, Object> details;

    public static CompanyCreatedEventBuilder eventFrom(CompanyCreationCmd cmd) {
        return CompanyCreatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails());
    }

    public static CompanyCreationCmdBuilder cmdFrom(CompanyCreationCmd cmd) {
        return CompanyCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails());
    }

    public static CompanyCreationCmdBuilder cmdFrom(CompanyUpdateCmd cmd) {
        return CompanyCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails());
    }


    @Override
    public ValidationResult<CompanyCreationCmd> validate(AggregateHelper util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtil.isNullOrEmpty(id)) {
            errors.add("Company identifier shouldn't be null or empty");
        } else {
            util.aggregateGet(id, CompanyAggregate.class).ifPresent(c -> errors.add("The company with id " + id + " already exist"));
        }
        if (StringUtil.isNullOrEmpty(description)) {
            errors.add("Company description shouldn't be null or empty");
        }
        return buildResult(errors);
    }
}
