package com.ia.operation.handlers.event;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.ia.operation.documents.Period;
import com.ia.operation.events.created.PeriodCreatedEvent;
import com.ia.operation.helper.ProjectionGenerator;
import com.ia.operation.queries.period.PeriodGetByIdQuery;
import com.ia.operation.queries.period.PeriodGetByYearQuery;
import com.ia.operation.repositories.PeriodRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@AllArgsConstructor
@Slf4j
@ProcessingGroup("period-handler")
public class PeriodEventHandler {
    private final PeriodRepository periodRepository;
    private final ProjectionGenerator generator;

    @EventHandler
    public void on(PeriodCreatedEvent event) {
        final Period period = Period.from(event);
        log.info("Event recieved: value=[{}]", period);
        periodRepository.save(period).subscribe(p -> {
            log.info("Period succesfully saved. value=[{}]", p);
            generator.generate(p);
        });
    }
    
    @QueryHandler
    public CompletableFuture<Period> periodGetById(PeriodGetByIdQuery query) {
        log.info("periodGetById query recieved: value=[{}]", query);
        return periodRepository.findById(query.getPeriodId()).toFuture();
    }
    
    @QueryHandler
    public CompletableFuture<List<Period>> periodGetByYearQuery(PeriodGetByYearQuery query) {
        log.info("periodGetByYear query recieved: value=[{}]", query);
        return periodRepository.findByYear(query.getYear()).collectList().toFuture();
    }
}
