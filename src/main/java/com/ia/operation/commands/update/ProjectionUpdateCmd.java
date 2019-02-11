package com.ia.operation.commands.update;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectionUpdateCmd {
    @TargetAggregateIdentifier
    private String id;
    @JsonProperty("operation_id")
    private String operationId;
    private BigDecimal amount;
    @JsonProperty("period_id")
    private String periodId;
    
    public static ProjectionUpdateCmdBuilder from (ProjectionUpdateCmd cmd) {
        return ProjectionUpdateCmd.builder()
                .amount(cmd.getAmount())
                .id(cmd.getId())
                .operationId(cmd.getOperationId())
                .periodId(cmd.getPeriodId());
    }

}
