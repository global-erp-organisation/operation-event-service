package com.ia.operation.documents;

import java.time.LocalDate;
import java.util.Collection;

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
    @Indexed(unique = false)
    private Integer year;
    private LocalDate start;
    private LocalDate end;
    private String description;
    private Collection<Realisation> realisations;
    private Collection<Projection> projections;
    @Builder.Default
    private Boolean close = false;

    public static Period from(PeriodCreatedEvent event) {
        return Period.builder()
                .id(event.getId())
                .year(event.getYear())
                .start(event.getStart())
                .end(event.getEnd())
                .description(event.getDescription())
                .close(event.getClose())
                .build();
    }
}
