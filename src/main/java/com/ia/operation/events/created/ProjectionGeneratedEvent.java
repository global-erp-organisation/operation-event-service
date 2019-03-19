package com.ia.operation.events.created;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectionGeneratedEvent implements Serializable {
    private String year;
    @Builder.Default
    private String routingKey = "event";
}
