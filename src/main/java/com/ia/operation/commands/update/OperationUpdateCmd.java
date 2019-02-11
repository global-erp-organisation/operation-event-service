package com.ia.operation.commands.update;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.enums.OperationType;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.events.updated.OperationUpdatedEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OperationUpdateCmd {
    @TargetAggregateIdentifier
    private String id;
    private String description;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("operation_type")
    private OperationType operationType;
    @JsonProperty("recurring_mode")
    private RecurringMode recurringMode;
    @JsonProperty("default_amount")
    private BigDecimal defaultAmount;
    private String categoryId;
    
    public static OperationUpdateCmdBuilder from(OperationUpdateCmd cmd) {
        return OperationUpdateCmd.builder()
                .defaultAmount(cmd.getDefaultAmount())
                .description(cmd.getDescription())
                .id(cmd.getId())
                .userId(cmd.getUserId())
                .recurringMode(cmd.getRecurringMode())
                .operationType(cmd.getOperationType())
                .categoryId(cmd.getCategoryId());
    }
    
    public static OperationUpdatedEvent.OperationUpdatedEventBuilder of(OperationUpdateCmd cmd) {
        return OperationUpdatedEvent.builder()
                .defaultAmount(cmd.getDefaultAmount())
                .description(cmd.getDescription())
                .id(cmd.getId())
                .userId(cmd.getUserId())
                .recurringMode(cmd.getRecurringMode())
                .operationType(cmd.getOperationType())
                .categoryId(cmd.getCategoryId());
    }

}
