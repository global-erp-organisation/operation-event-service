package com.ia.operation.commands.update;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.aggregates.AccountAggregate;
import com.ia.operation.aggregates.PeriodAggregate;
import com.ia.operation.aggregates.ProjectionAggregate;
import com.ia.operation.enums.OperationType;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ProjectionUpdateCmd extends CommandValidator<ProjectionUpdateCmd> {
    @TargetAggregateIdentifier
    protected String id;

    @JsonProperty("account_id")
    private String accountId;
    private BigDecimal amount;
    @JsonProperty("period_id")
    private String periodId;
    private OperationType operationType;

    public static ProjectionUpdateCmdBuilder cmdFrom(ProjectionUpdateCmd cmd) {
        return ProjectionUpdateCmd.builder()
                .amount(cmd.getAmount())
                .id(cmd.getId())
                .accountId(cmd.getAccountId())
                .periodId(cmd.getPeriodId())
                .operationType(cmd.getOperationType());
    }

    @Override
    public ValidationResult<ProjectionUpdateCmd> validate(AggregateHelper util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Projection identifier shouldn't be null or empty");
        } else {
            final Optional<ProjectionAggregate> p = util.aggregateGet(id, ProjectionAggregate.class);
            if (p.isPresent()) {
                setPeriodId(periodId == null ? p.get().getPeriodId() : periodId);
                setAccountId(accountId == null ? p.get().getAccountId() : accountId);
            } else {
                errors.add("The projection with id " + id + " doesnt exist");
            }
        }
        if (StringUtil.isNullOrEmpty(accountId)) {
            errors.add("account identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(accountId, AccountAggregate.class).isPresent()) {
                errors.add("The account with id " + id + " doesnt exist");
            }
        }
        if (StringUtil.isNullOrEmpty(periodId)) {
            errors.add("period identifier shouldn't be null or empty");
        } else {
            if (!util.aggregateGet(periodId, PeriodAggregate.class).isPresent()) {
                errors.add("The period with id " + id + " doesnt exist");
            }
        }
        return buildResult(errors);
    }
}
