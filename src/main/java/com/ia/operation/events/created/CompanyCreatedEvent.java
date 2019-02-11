package com.ia.operation.events.created;

import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompanyCreatedEvent {
    private String id;
    private String description;
    private Map<String, Object> details;
}
