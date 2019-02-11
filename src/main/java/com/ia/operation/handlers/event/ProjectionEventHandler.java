package com.ia.operation.handlers.event;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.ia.operation.documents.Projection;
import com.ia.operation.events.created.ProjectionCreatedEvent;
import com.ia.operation.events.created.ProjectionGeneratedEvent;
import com.ia.operation.queries.projection.ProjectionByOperationQuery;
import com.ia.operation.queries.projection.ProjectionByPeriodQuery;
import com.ia.operation.queries.projection.ProjectionGenerationQuery;
import com.ia.operation.repositories.OperationRepository;
import com.ia.operation.repositories.PeriodRepository;
import com.ia.operation.repositories.ProjectionRepository;
import com.ia.operation.util.ProjectionGenerator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
@ProcessingGroup("projection-handler")
public class ProjectionEventHandler {

    private final ProjectionRepository projectionRepository;
    private final OperationRepository OperationRepository;
    private final PeriodRepository PeriodRepository;
    private final ProjectionGenerator generator;
    private final RabbitTemplate rabbitTemplate;

    @EventHandler
    public void on(ProjectionCreatedEvent event) {
        log.info("Event recieved: value=[{}]", event);
        PeriodRepository.findById(event.getPeriodId()).subscribe(p -> {
            OperationRepository.findById(event.getOperationId()).subscribe(o -> {
                projectionRepository.save(Projection.of(event, o, p)).subscribe(pr -> {
                    log.info("Projection succesfully saved. value=[{}]", pr);
                });
            });
        });
    }

    @RabbitListener(queues = {"projection-cmd-queue"})
    public void on(ProjectionGeneratedEvent event) {
        generator.generate(event.getYear()).collectList().subscribe(e -> {
            rabbitTemplate.convertAndSend("event", e);
        });
    }

    @QueryHandler
    public Object projectionByOperation(ProjectionByOperationQuery query) {
        return projectionRepository.findByOperation_IdAndOperation_UserId(query.getOperationId(), query.getUserId());
    }

    @QueryHandler
    public Object projectionByPeriod(ProjectionByPeriodQuery query) {
        return projectionRepository.findByPeriod_IdAndOperation_UserIdOrderByOperation_description(query.getPeriodId(), query.getUserId());
    }

    @QueryHandler
    public Object projectionGenerate(ProjectionGenerationQuery query) {
        return generator.generate(query.getYear());
    }
}
