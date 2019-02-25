package com.ia.operation.commands.update;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.aggregates.CompanyAggregate;
import com.ia.operation.aggregates.AccountAggregate;
import com.ia.operation.aggregates.PeriodAggregate;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class ProjectionUpdateCmd extends CommandValidator<ProjectionUpdateCmd>{
    @TargetAggregateIdentifier
    private String id;
    @JsonProperty("account_id")
    private String accountId;
    private BigDecimal amount;
    @JsonProperty("period_id")
    private String periodId;
    
    public static ProjectionUpdateCmdBuilder from (ProjectionUpdateCmd cmd) {
        return ProjectionUpdateCmd.builder()
                .amount(cmd.getAmount())
                .id(cmd.getId())
                .accountId(cmd.getAccountId())
                .periodId(cmd.getPeriodId());
    }
    
    @Override
    public ValidationResult<ProjectionUpdateCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Projection identifier shouldn't be null or empty");
        }else {
            if (!util.aggregateGet(id, CompanyAggregate.class).isPresent()) {
                errors.add("The projection with id " + id + " doesnt exist");
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
