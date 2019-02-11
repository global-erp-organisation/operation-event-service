package com.ia.operation.commands.update;

import java.util.Date;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PeriodUpdateCmd {
    @TargetAggregateIdentifier
    private String id;
    private Date start;
    private Date end;
}
