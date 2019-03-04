package com.ia.operation.events.updated;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountCategoryUpdatedEvent {
    private String id;
    private String description;
}
