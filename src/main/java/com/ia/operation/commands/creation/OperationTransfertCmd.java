package com.ia.operation.commands.creation;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.helper.validator.CommandValidator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = true)
public class OperationTransfertCmd extends CommandValidator<OperationCreationCmd> {
    @TargetAggregateIdentifier
    protected String id;
     String description;
    @JsonProperty("source_account_id")
     String sourceAccountId;
    @JsonProperty("target_account_id")
     String targetAccountId;
    @JsonProperty("operation_date")
     LocalDate operationDate;
     BigDecimal amount;
}
