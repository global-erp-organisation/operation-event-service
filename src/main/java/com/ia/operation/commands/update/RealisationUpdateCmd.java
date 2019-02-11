package com.ia.operation.commands.update;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.events.updated.RealisationUpdatedEvent;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RealisationUpdateCmd {
    private String id;
    private String description;
    @JsonProperty("operation_id")
    private String operationId;
    @JsonProperty("operation_date")
    private LocalDate operationDate;
    @JsonProperty("period_id")
    private String periodId;
    private BigDecimal amount;

    public static RealisationUpdateCmdBuilder from(RealisationUpdateCmd cmd) {
        return RealisationUpdateCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .operationDate(cmd.getOperationDate())
                .operationId(cmd.getOperationId())
                .amount(cmd.getAmount());
    }

    
    public static RealisationUpdatedEvent.RealisationUpdatedEventBuilder of(RealisationUpdateCmd cmd) {
        return RealisationUpdatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .operationDate(cmd.getOperationDate())
                .operationId(cmd.getOperationId())
                .amount(cmd.getAmount());
    }

}
