package com.ia.operation.handlers.event;

import com.ia.operation.documents.Account;
import com.ia.operation.documents.Period;
import com.ia.operation.documents.Projection;
import com.ia.operation.events.created.ProjectionCreatedEvent;
import com.ia.operation.events.created.ProjectionGeneratedEvent;
import com.ia.operation.helper.ProjectionGenerator;
import com.ia.operation.queries.projection.ProjectionByAccountQuery;
import com.ia.operation.queries.projection.ProjectionByYearQuery;
import com.ia.operation.queries.projection.ProjectionGenerationQuery;
import com.ia.operation.repositories.AccountRepository;
import com.ia.operation.repositories.PeriodRepository;
import com.ia.operation.repositories.ProjectionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@AllArgsConstructor
@Slf4j
@ProcessingGroup("projection-handler")
public class ProjectionEventHandler {

    private final ProjectionRepository projectionRepository;
    private final AccountRepository AccountRepository;
    private final PeriodRepository PeriodRepository;
    private final ProjectionGenerator generator;

    @EventHandler
    public void on(ProjectionCreatedEvent event) {
        log.info("Event recieved: value=[{}]", event);
        final Mono<Period> period = PeriodRepository.findById(event.getPeriodId());
        final Mono<Account> account = AccountRepository.findById(event.getAccountId());
        period.subscribe(p -> account.subscribe(o -> projectionRepository.save(Projection.of(event, o, p)).subscribe(pr -> log.info("Projection succesfully saved. value=[{}]", pr))));
    }

    @RabbitListener(queues = {"${axon.events.projection-cmd-queue}"})
    public void on(ProjectionGeneratedEvent event) {
        generator.generate(event.getYear());
    }

    @QueryHandler
    public CompletableFuture<List<Projection>> projectionByAccount(ProjectionByAccountQuery query) {
        log.info("ProjectionByAccount query recieved: value=[{}]", query);
        return projectionRepository.findByAccount_IdAndPeriod_yearOrderByPeriod_start(query.getAccountId(), query.getYear()).collectList().toFuture();
    }

    @QueryHandler
    public CompletableFuture<List<Projection>> projectionByyear(ProjectionByYearQuery query) {
        log.info("ProjectionByYear query recieved: value=[{}]", query);
        return projectionRepository.findByAccount_User_IdAndPeriod_yearOrderByPeriod_start(query.getUserId(), query.getYear()).collectList().toFuture();
    }

    @QueryHandler
    public CompletableFuture<List<Projection>> projectionGenerate(ProjectionGenerationQuery query) {
        log.info("ProjectionGeneration query recieved: value=[{}]", query);
        return projectionRepository.findByAccount_User_IdAndPeriod_yearOrderByPeriod_start(query.getUserId(), query.getYear()).collectList().toFuture();
    }
}
