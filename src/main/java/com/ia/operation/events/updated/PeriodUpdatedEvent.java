package com.ia.operation.events.updated;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PeriodUpdatedEvent {
    private String id;
    private int year;
}
