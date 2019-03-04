package com.ia.operation.events.created;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountCategoryCreatedEvent {
    private String id;
    private String description;
}
