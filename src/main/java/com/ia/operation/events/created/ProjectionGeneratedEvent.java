package com.ia.operation.events.created;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@SuppressWarnings("serial")
@Value
@Builder
public class ProjectionGeneratedEvent implements Serializable {
    private String year;
    @Builder.Default
    private String routingKey = "event";
}
