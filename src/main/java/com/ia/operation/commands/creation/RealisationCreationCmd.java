package com.ia.operation.commands.creation;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.events.created.RealisationCreatedEvent;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RealisationCreationCmd {
    @TargetAggregateIdentifier
    private String id;
    private String description;
    @JsonProperty("operation_id")
    private String operationId;
    @JsonProperty("operation_date")
    private LocalDate operationDate;
    private BigDecimal amount;
    
    public static RealisationCreationCmdBuilder from(RealisationCreationCmd cmd) {
        return RealisationCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .operationDate(cmd.getOperationDate())
                .operationId(cmd.getOperationId())
                .amount(cmd.getAmount());
    }
    
    public static RealisationCreatedEvent.RealisationCreatedEventBuilder of(RealisationCreationCmd cmd) {
        return RealisationCreatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .operationDate(cmd.getOperationDate())
                .operationId(cmd.getOperationId())
                .amount(cmd.getAmount());
    }

}
