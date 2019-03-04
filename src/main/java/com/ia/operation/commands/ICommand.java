package com.ia.operation.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public interface ICommand {
    @TargetAggregateIdentifier
    String getId();
}
