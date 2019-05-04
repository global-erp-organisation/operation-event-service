package com.ia.operation.helper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import com.ia.operation.documents.Period;
import com.ia.operation.events.created.PeriodCreatedEvent;

import reactor.core.publisher.Mono;

public interface PeriodGenerator {
   
    Collection<PeriodCreatedEvent> generate(String year);
    
    Mono<Optional<Period>> resolve(LocalDate date, String year);
    
    default Optional<Period> resolve(LocalDate date, Collection<Period> periods) {
        return periods.stream()
                .filter(p -> (p.getStart().toEpochDay() <= date.toEpochDay()) && (p.getEnd().toEpochDay() >= date.toEpochDay()))
                .findFirst();

    }
    
    default Optional<Period> resolve(String month, Collection<Period> periods) {
        return periods.stream()
                .filter(p -> p.getDescription().equals(month))
                .findFirst();
    }
}
