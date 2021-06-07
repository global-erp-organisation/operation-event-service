package com.ia.operation.documents;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.events.created.PeriodCreatedEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Document
@Builder
@AllArgsConstructor
public class Period {
    @Id
    private String id;
    @Indexed
    private String year;
    private LocalDate start;
    private LocalDate end;
    private String description;
    @Builder.Default
    private Boolean close = false;

    public static Period from(PeriodCreatedEvent event) {
        /*@formatter:off*/
        return Period.builder()
                .id(event.getId())
                .year(event.getYear())
                .start(event.getStart())
                .end(event.getEnd())
                .description(event.getDescription())
                .close(event.isClose())
                .build();
        /*@formatter:on*/
    }
    
    public Boolean contains(LocalDate date) {
        return (getStart().toEpochDay() <= date.toEpochDay()) && (getEnd().toEpochDay() >= date.toEpochDay());
    }
}
