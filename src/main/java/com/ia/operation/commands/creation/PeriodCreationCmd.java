package com.ia.operation.commands.creation;

import java.time.LocalDate;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ia.operation.events.created.PeriodCreatedEvent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeriodCreationCmd {
    @TargetAggregateIdentifier
    private String id;
    private int year;
    private String description;
    private LocalDate start;
    private LocalDate end;
    private Boolean close;
    
    @JsonIgnore
    private PeriodCreatedEvent event;
    
    public static PeriodCreatedEvent.PeriodCreatedEventBuilder of(PeriodCreationCmd cmd) {
        return PeriodCreatedEvent.builder()
                .year(cmd.getYear())
                .id(cmd.getId())
                .start(cmd.getStart())
                .end(cmd.getEnd())
                .close(cmd.getClose());
    }
    
    public static PeriodCreationCmdBuilder buildFrom(PeriodCreationCmd cmd) {
        return PeriodCreationCmd.builder()
                .year(cmd.getYear())
                .id(cmd.getId())
                .start(cmd.getStart())
                .end(cmd.getEnd())
                 .close(cmd.getClose());
    }
    
    public static PeriodCreationCmdBuilder from(PeriodCreatedEvent event) {
        return PeriodCreationCmd.builder()
                .id(event.getId())
                .year(event.getYear())
                .description(event.getDescription())
                .start(event.getStart())
                .end(event.getEnd())
                .event(event)
                .close(event.getClose());
    }
}
