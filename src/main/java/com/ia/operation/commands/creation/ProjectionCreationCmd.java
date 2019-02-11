package com.ia.operation.commands.creation;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.events.created.ProjectionCreatedEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectionCreationCmd {
    @TargetAggregateIdentifier
    private String id;
    @JsonProperty("operation_id")
    private String operationId;
    private BigDecimal amount;
    @JsonProperty("period_id")
    private String periodId;
    @JsonIgnore
    private ProjectionCreatedEvent event;
    
    public static ProjectionCreationCmdBuilder from(ProjectionCreationCmd cmd) {
        return ProjectionCreationCmd.builder()
                .id(cmd.getId())
                .operationId(cmd.getOperationId())
                .periodId(cmd.getPeriodId())
                .amount(cmd.getAmount());
    }
    
    public static ProjectionCreatedEvent of(ProjectionCreationCmd cmd) {
        return ProjectionCreatedEvent.builder()
                .id(cmd.getId())
                .operationId(cmd.getOperationId())
                .periodId(cmd.getPeriodId())
                .amount(cmd.getAmount())
                .build();
    }
    
    public static ProjectionCreationCmdBuilder from(ProjectionCreatedEvent event) {
        return ProjectionCreationCmd.builder()
                .id(event.getId())
                .operationId(event.getOperationId())
                .periodId(event.getPeriodId())
                .amount(event.getAmount())
                .event(event);
    }

}
