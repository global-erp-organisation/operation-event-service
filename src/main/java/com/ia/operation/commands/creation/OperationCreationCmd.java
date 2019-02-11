package com.ia.operation.commands.creation;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.enums.OperationType;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.events.created.OperationCreatedEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OperationCreationCmd {
    @TargetAggregateIdentifier
    private String id;
    private String description;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("operation_type")
    @Builder.Default
    private OperationType operationType = OperationType.UNDEFINED;
    @JsonProperty("recurring_mode")
    @Builder.Default
    private RecurringMode recurringMode = RecurringMode.NONE;
    @JsonProperty("default_amount")
    private BigDecimal defaultAmount;
    private String categoryId;
    
    public static OperationCreationCmdBuilder from(OperationCreationCmd cmd) {
        return OperationCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .userId(cmd.getUserId())
                .recurringMode(cmd.getRecurringMode())
                .defaultAmount(cmd.getDefaultAmount())
                .operationType(cmd.getOperationType())
                .categoryId(cmd.getCategoryId());
    }
    
    
    public static OperationCreatedEvent of(OperationCreationCmd cmd) {
        return OperationCreatedEvent.builder()
                .id(cmd.getId())
                .userId(cmd.getUserId())
                .description(cmd.getDescription())
                .recurringMode(cmd.getRecurringMode())
                .operationType(cmd.getOperationType())
                .defaultAmount(cmd.getDefaultAmount())
                .categoryId(cmd.getCategoryId())
                .build();
    }
}
