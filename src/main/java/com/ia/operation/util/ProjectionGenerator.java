package com.ia.operation.util;

import com.ia.operation.events.created.ProjectionCreatedEvent;

import reactor.core.publisher.Flux;

public interface ProjectionGenerator {
    Flux<ProjectionCreatedEvent> generate(String year);
}
