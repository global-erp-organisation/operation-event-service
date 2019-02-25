package com.ia.operation.commands.update;

import java.time.LocalDate;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PeriodUpdateCmd {
    @TargetAggregateIdentifier
    private String id;
    private LocalDate start;
    private LocalDate end;
    private String year;
}
