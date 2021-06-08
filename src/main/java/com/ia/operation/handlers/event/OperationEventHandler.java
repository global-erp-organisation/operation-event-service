package com.ia.operation.handlers.event;

import com.corundumstudio.socketio.SocketIOServer;
import com.ia.operation.configuration.websocket.WebSocketConfiguration;
import com.ia.operation.documents.Account;
import com.ia.operation.documents.Operation;
import com.ia.operation.documents.Period;
import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.documents.views.MonthlyHistoryView;
import com.ia.operation.enums.WebSocketEvents;
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
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
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
    private final HistoryUpdater<DailyHistoryView, Operation> dailyUpdater;
    private final HistoryUpdater<MonthlyHistoryView, Operation> monthlyUpdate;
    private final SocketIOServer socketIOServer;

    @EventHandler
    public void on(OperationCreatedEvent event) {
        log.info("event received: [{}]", event);
        final Flux<Period> periods =
                periodRepository.findByYear(String.valueOf(event.getOperationDate().getYear())).filter(p -> p.contains(event.getOperationDate()));
        final Mono<Account> acc = accountRepository.findById(event.getAccountId());
        periods.subscribe(period -> acc.subscribe(account -> operationRepository.save(Operation.of(event, period, account)).subscribe(operation -> {
            log.info("Operation successfully saved. [{}]", operation);
            dailyUpdater.update(operation, operation, UpdateType.A);
            monthlyUpdate.update(operation, operation, UpdateType.A);
            notifyClient(operation);
        })));
    }

    @EventHandler
    public void on(OperationUpdatedEvent event) {
        log.info("event received: [{}]", event);
        final Flux<Period> periods =
                periodRepository.findByYear(String.valueOf(event.getOperationDate().getYear())).filter(p -> p.contains(event.getOperationDate()));
        final Mono<Account> account = accountRepository.findById(event.getAccountId());
        periods.subscribe(period -> account.subscribe(operation -> operationRepository.findById(event.getId()).subscribe(old -> operationRepository.save(Operation.of(event, period, operation)).subscribe(current -> {
            log.info("Operation successfully updated. [{}]", current);
            dailyUpdater.update(current, old, UpdateType.U);
            monthlyUpdate.update(current, old, UpdateType.U);
            notifyClient(current);
        }))));
    }

    @EventHandler
    public void on(OperationDeletedEvent event) {
        log.info("event received: [{}]", event);
        operationRepository.findById(event.getId()).subscribe(old -> operationRepository.deleteById(event.getId()).subscribe(e -> {
            log.info("Operation successfully removed. [{}]", event.getId());
            dailyUpdater.update(old, old, UpdateType.R);
            monthlyUpdate.update(old, old, UpdateType.R);
            notifyClient(old);
        }));
    }

    private void notifyClient(Operation o) {
        WebSocketConfiguration.userIdToSessionId(o.getAccount().getUser().getId()).ifPresent(sessionId -> socketIOServer.getClient(sessionId).sendEvent(WebSocketEvents.DASHBOARD.name()));
    }
}
