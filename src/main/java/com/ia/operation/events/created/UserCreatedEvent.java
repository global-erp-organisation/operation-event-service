package com.ia.operation.events.created;

import java.util.Map;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserCreatedEvent {
    @TargetAggregateIdentifier
    private String id;
    private String email;
    private String description;
    private Map<String, Object> details;
    private String companyId;
    private String password;
}
