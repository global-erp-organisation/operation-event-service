package com.ia.operation.commands.creation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.aggregates.AccountAggregate;
import com.ia.operation.aggregates.CompanyAggregate;
import com.ia.operation.aggregates.PeriodAggregate;
import com.ia.operation.enums.OperationType;
import com.ia.operation.events.created.ProjectionCreatedEvent;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class ProjectionCreationCmd extends CommandValidator<ProjectionCreationCmd>{
    @TargetAggregateIdentifier
    protected String id;

    @JsonProperty("account_id")
    private String accountId;
    private BigDecimal amount;
    @JsonProperty("period_id")
    private String periodId;
    private OperationType operationType;
    @JsonIgnore
    private ProjectionCreatedEvent event;

    public static ProjectionCreationCmdBuilder cmdFrom(ProjectionCreationCmd cmd) {
        return ProjectionCreationCmd.builder()
                .id(cmd.getId())
                .accountId(cmd.getAccountId())
                .periodId(cmd.getPeriodId())
                .amount(cmd.getAmount())
                .operationType(cmd.getOperationType());
    }
    
    public static ProjectionCreatedEvent eventFrom(ProjectionCreationCmd cmd) {
        return ProjectionCreatedEvent.builder()
                .id(cmd.getId())
                .accountId(cmd.getAccountId())
                .periodId(cmd.getPeriodId())
                .amount(cmd.getAmount())
                .operationType(cmd.getOperationType())
                .build();
    }
    
    public static ProjectionCreationCmdBuilder cmdFrom(ProjectionCreatedEvent event) {
        return ProjectionCreationCmd.builder()
                .id(event.getId())
                .accountId(event.getAccountId())
                .periodId(event.getPeriodId())
                .amount(event.getAmount())
                .operationType(event.getOperationType())
                .event(event);
    }
    
    @Override
    public ValidationResult<ProjectionCreationCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Projection identifier shouldn't be null or empty");
        }else {
            if (util.aggregateGet(id, CompanyAggregate.class).isPresent()) {
                errors.add("The projection with id " + id + " already exist");
            }
        }
        if (StringUtil.isNullOrEmpty(accountId)) {
            errors.add("account identifier shouldn't be null or empty");
        }else {
            if (!util.aggregateGet(accountId, AccountAggregate.class).isPresent()) {
                errors.add("The account with id " + id + " doesnt exist");
            }
        }       
        if (StringUtil.isNullOrEmpty(periodId)) {
            errors.add("period identifier shouldn't be null or empty");
        }else {
            if (!util.aggregateGet(periodId, PeriodAggregate.class).isPresent()) {
                errors.add("The period with id " + id + " doesnt exist");
            }
        }
        return buildResult(errors);
    }
}
