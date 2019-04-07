package com.ia.operation.handlers.event;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.ia.operation.documents.Account;
import com.ia.operation.documents.Operation;
import com.ia.operation.documents.Period;
import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.documents.views.MonthlyHistoryView;
import com.ia.operation.events.created.OperationCreatedEvent;
import com.ia.operation.events.deleted.OperationDeletedEvent;
import com.ia.operation.events.updated.OperationUpdatedEvent;
import com.ia.operation.helper.history.HistoryUpdater;
import com.ia.operation.helper.history.UpdateType;
import com.ia.operation.repositories.AccountRepository;
import com.ia.operation.repositories.OperationRepository;
import com.ia.operation.repositories.PeriodRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@ProcessingGroup("operation-handler")
@RequiredArgsConstructor
@Slf4j
public class OperationEventHandler {

    private final AccountRepository accountRepository;
    private final PeriodRepository periodRepository;
    private final OperationRepository operationRepository;
    private final HistoryUpdater<DailyHistoryView> dailyUpdater;
    private final HistoryUpdater<MonthlyHistoryView> monthlyUpdate;
    
    @EventHandler
    public void on(OperationCreatedEvent event) {
        log.info("event recieved: [{}]", event);
        final Flux<Period> periods =
                periodRepository.findByYear(String.valueOf(event.getOperationDate().getYear())).filter(p -> p.contains(event.getOperationDate()));
        final Mono<Account> acc = accountRepository.findById(event.getAccountId());
        periods.subscribe(period -> {
            acc.subscribe(account -> {
                operationRepository.save(Operation.of(event, period, account)).subscribe(opration -> {
                    log.info("Operation succesfully saved. [{}]", opration);
                    dailyUpdater.update(opration, opration, UpdateType.A);
                    monthlyUpdate.update(opration, opration, UpdateType.A);
                });
            });
        });
    }

    @EventHandler
    public void on(OperationUpdatedEvent event) {
        log.info("event recieved: [{}]", event);
        final Flux<Period> periods =
                periodRepository.findByYear(String.valueOf(event.getOperationDate().getYear())).filter(p -> p.contains(event.getOperationDate()));
        final Mono<Account> account = accountRepository.findById(event.getAccountId());
        periods.subscribe(period -> {
            account.subscribe(operation -> {
                operationRepository.findById(event.getId()).subscribe(old -> {
                    operationRepository.save(Operation.of(event, period, operation)).subscribe(current -> {
                        log.info("Operation succesfully upodated. [{}]", current);
                        dailyUpdater.update(current, old, UpdateType.U);
                        monthlyUpdate.update(current, old, UpdateType.U);
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
             });
            dailyUpdater.update(old, old, UpdateType.R);
            monthlyUpdate.update(old, old, UpdateType.R);
        });
    }
}
