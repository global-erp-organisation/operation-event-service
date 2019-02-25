package com.ia.operation.commands.creation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.operation.aggregates.CompanyAggregate;
import com.ia.operation.commands.update.CompanyUpdateCmd;
import com.ia.operation.events.created.CompanyCreatedEvent;
import com.ia.operation.events.created.CompanyCreatedEvent.CompanyCreatedEventBuilder;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class CompanyCreationCmd extends CommandValidator<CompanyCreationCmd>{
    @TargetAggregateIdentifier
    private String id;
    private String description;
    private Map<String, Object> details;
    
    public static CompanyCreatedEventBuilder from(CompanyCreationCmd cmd) {
        return CompanyCreatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails());
    }
    public static CompanyCreationCmdBuilder of(CompanyCreationCmd cmd) {
        return CompanyCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails());
    }
    
    public static CompanyCreationCmdBuilder of(CompanyUpdateCmd cmd) {
        return CompanyCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails());
    }
    
    
    @Override
    public ValidationResult<CompanyCreationCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Company identifier shouldn't be null or empty");
        }else {
            if (util.aggregateGet(id, CompanyAggregate.class).isPresent()) {
                errors.add("The company with id " + id + " already exist");
            }
        }
        if (StringUtil.isNullOrEmpty(description)) {
            errors.add("Company description shouldn't be null or empty");
        }
        return buildResult(errors);
    }

}
