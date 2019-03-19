package com.ia.operation.events.created;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PeriodCreatedEvent implements Serializable {
    private String id;
    private String year;
    private LocalDate start;
    private LocalDate end;
    private String description;
    private Boolean close;
    
    
    public static PeriodCreatedEvent.PeriodCreatedEventBuilder of(PeriodCreatedEvent period, Locale locale) {
        return PeriodCreatedEvent.builder()
                .year(period.getYear())
                .description(period.getStart().getMonth().getDisplayName(TextStyle.FULL, locale) + "-" + period.getStart().getYear())
                .end(period.getEnd())
                .id(period.getId())
                .start(period.getStart())
                .close(period.getClose());
    }
    
    public static PeriodCreatedEvent.PeriodCreatedEventBuilder of(PeriodCreatedEvent period) {
        return of(period, Locale.US);
    }
}
