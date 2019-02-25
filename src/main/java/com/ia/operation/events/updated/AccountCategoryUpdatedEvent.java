package com.ia.operation.events.updated;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountCategoryUpdatedEvent {
    @TargetAggregateIdentifier
    private String id;
    private String description;
}