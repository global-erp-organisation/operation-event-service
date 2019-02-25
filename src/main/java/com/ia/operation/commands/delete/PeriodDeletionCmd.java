package com.ia.operation.commands.delete;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PeriodDeletionCmd {
    @TargetAggregateIdentifier
    private String id;
    private int year;
}
