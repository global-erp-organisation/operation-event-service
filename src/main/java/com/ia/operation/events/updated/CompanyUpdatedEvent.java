package com.ia.operation.events.updated;

import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompanyUpdatedEvent {
    private String id;
    private String description;
    private Map<String, Object> details;
}
