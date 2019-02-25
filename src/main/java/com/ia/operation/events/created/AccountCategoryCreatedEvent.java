package com.ia.operation.events.created;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountCategoryCreatedEvent {
    @TargetAggregateIdentifier
    private String id;
    private String description;
}
