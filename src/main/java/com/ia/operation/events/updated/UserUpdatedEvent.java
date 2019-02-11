package com.ia.operation.events.updated;

import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserUpdatedEvent {
    private String id;
    private String email;
    private String description;
    private Map<String, Object> details;
    private String companyId;
    private String password;
}
