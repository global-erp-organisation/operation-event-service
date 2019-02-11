package com.ia.operation.commands.delete;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDeleteCmd {
    @TargetAggregateIdentifier
    private String id;
}
