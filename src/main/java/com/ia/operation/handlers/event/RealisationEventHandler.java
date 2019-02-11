package com.ia.operation.handlers.event;

import java.time.LocalDate;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.ia.operation.documents.Period;
import com.ia.operation.documents.Realisation;
import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.documents.views.MonthlyHistoryView;
import com.ia.operation.documents.views.YearlyHistoryView;
import com.ia.operation.events.created.RealisationCreatedEvent;
import com.ia.operation.events.deleted.RealisationDeletedEvent;
import com.ia.operation.events.updated.RealisationUpdatedEvent;
import com.ia.operation.repositories.OperationRepository;
import com.ia.operation.repositories.PeriodRepository;
import com.ia.operation.repositories.RealisationRepository;
import com.ia.operation.util.history.HistoryUpdater;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@ProcessingGroup("relaisation-handler")
@AllArgsConstructor
@Slf4j
public class RealisationEventHandler {
    private final OperationRepository operationRepository;
    private final PeriodRepository periodRepository;
    private final RealisationRepository realisationRepository;
    private final HistoryUpdater<DailyHistoryView> dailyUpdater;
    private final HistoryUpdater<MonthlyHistoryView> monthlyUpdater;
    private final HistoryUpdater<YearlyHistoryView> yearlyUpdater;

    @EventHandler
    public void on(RealisationCreatedEvent event) {
        log.info("event recieved: [{}]", event);
        periodRepository.findByYear(event.getOperationDate().getYear()).filter(p -> isBelongs(p, event.getOperationDate())).subscribe(period -> {
            operationRepository.findById(event.getOperationId()).subscribe(operation -> {
                realisationRepository.save(Realisation.of(event, period, operation)).subscribe(realisation -> {
                    log.info("Realisation succesfully saved. [{}]", realisation);
                    dailyUpdater.update(realisation);
                    monthlyUpdater.update(realisation);
                    yearlyUpdater.update(realisation);
                });
            });
        });
    }

    @EventHandler
    public void on(RealisationUpdatedEvent event) {
        log.info("event recieved: [{}]", event);
        periodRepository.findByYear(event.getOperationDate().getYear()).filter(p -> isBelongs(p, event.getOperationDate())).subscribe(period -> {
            operationRepository.findById(event.getOperationId()).subscribe(operation -> {
                realisationRepository.save(Realisation.of(event, period, operation)).subscribe(realisation -> {
                    log.info("Realisation succesfully updated. [{}]", realisation);
                    dailyUpdater.update(realisation);
                    monthlyUpdater.update(realisation);
                    yearlyUpdater.update(realisation);
                });
            });
        });
    }

    private Boolean isBelongs(Period p, LocalDate date) {
        return (p.getStart().toEpochDay() <= date.toEpochDay()) && (p.getEnd().toEpochDay() >= date.toEpochDay());
    }

    @EventHandler
    public void on(RealisationDeletedEvent event) {
        log.info("event recieved: [{}]", event);
        realisationRepository.deleteById(event.getId()).subscribe(e -> {
            log.info("Realisation succesfully removed. [{}]", event.getId());
        });
    }
}
