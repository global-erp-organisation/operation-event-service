package com.ia.operation.handlers.event;

import java.time.LocalDate;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.ia.operation.documents.Account;
import com.ia.operation.documents.Operation;
import com.ia.operation.documents.Period;
import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.events.created.OperationCreatedEvent;
import com.ia.operation.events.deleted.OperationDeletedEvent;
import com.ia.operation.events.updated.OperationUpdatedEvent;
import com.ia.operation.repositories.AccountRepository;
import com.ia.operation.repositories.OperationRepository;
import com.ia.operation.repositories.PeriodRepository;
import com.ia.operation.util.history.HistoryUpdater;
import com.ia.operation.util.history.UpdateType;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@ProcessingGroup("relaisation-handler")
@AllArgsConstructor
@Slf4j
public class OperationEventHandler {

    private final AccountRepository accountRepository;
    private final PeriodRepository periodRepository;
    private final OperationRepository operationRepository;
    private final HistoryUpdater<DailyHistoryView> dailyUpdater;

    @EventHandler
    public void on(OperationCreatedEvent event) {
        log.info("event recieved: [{}]", event);
        final Flux<Period> periods =
                periodRepository.findByYear(String.valueOf(event.getOperationDate().getYear())).filter(p -> isBelongs(p, event.getOperationDate()));
        final Mono<Account> account = accountRepository.findById(event.getAccountId());
        periods.subscribe(period -> {
            account.subscribe(operation -> {
                operationRepository.save(Operation.of(event, period, operation)).subscribe(current -> {
                    log.info("Operation succesfully saved. [{}]", current);
                    dailyUpdater.update(current, current, UpdateType.A);
                });
            });
        });
    }

    @EventHandler
    public void on(OperationUpdatedEvent event) {
        log.info("event recieved: [{}]", event);
        final Flux<Period> periods =
                periodRepository.findByYear(String.valueOf(event.getOperationDate().getYear())).filter(p -> isBelongs(p, event.getOperationDate()));
        final Mono<Account> account = accountRepository.findById(event.getAccountId());
        periods.subscribe(period -> {
            account.subscribe(operation -> {
                operationRepository.findById(event.getId()).subscribe(old -> {
                    operationRepository.save(Operation.of(event, period, operation)).subscribe(current -> {
                        log.info("Operation succesfully upodated. [{}]", current);
                        dailyUpdater.update(current, old, UpdateType.U);
                    });
                });
            });
        });
    }

    @EventHandler
    public void on(OperationDeletedEvent event) {
        log.info("event recieved: [{}]", event);
        operationRepository.findById(event.getId()).subscribe(old -> {
            operationRepository.deleteById(event.getId()).subscribe(e -> {
                log.info("Operation succesfully removed. [{}]", event.getId());
                dailyUpdater.update(old, old, UpdateType.R);
             });
        });
    }

    private Boolean isBelongs(Period p, LocalDate date) {
        return (p.getStart().toEpochDay() <= date.toEpochDay()) && (p.getEnd().toEpochDay() >= date.toEpochDay());
    }

}
