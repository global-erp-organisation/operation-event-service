package com.ia.operation.commands.creation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ia.operation.aggregates.CompanyAggregate;
import com.ia.operation.events.created.PeriodCreatedEvent;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class PeriodCreationCmd extends CommandValidator<PeriodCreationCmd>{
    @TargetAggregateIdentifier
    protected String id;

    private String year;
    private String description;
    private LocalDate start;
    private LocalDate end;
    @Builder.Default
    private Boolean close = false;
    
    @JsonIgnore
    private PeriodCreatedEvent event;
    
    public static PeriodCreatedEvent.PeriodCreatedEventBuilder eventFrom(PeriodCreationCmd cmd) {
        return PeriodCreatedEvent.builder()
                .year(cmd.getYear())
                .id(cmd.getId())
                .start(cmd.getStart())
                .end(cmd.getEnd())
                .close(cmd.getClose());
    }
    
    public static PeriodCreationCmdBuilder cmdFrom(PeriodCreationCmd cmd) {
        return PeriodCreationCmd.builder()
                .year(cmd.getYear())
                .id(cmd.getId())
                .start(cmd.getStart())
                .end(cmd.getEnd())
                 .close(cmd.getClose());
    }
    
    public static PeriodCreationCmdBuilder cmdFrom(PeriodCreatedEvent event) {
        return PeriodCreationCmd.builder()
                .id(event.getId())
                .year(event.getYear())
                .description(event.getDescription())
                .start(event.getStart())
                .end(event.getEnd())
                .event(event)
                .close(event.getClose());
    }
    
    @Override
    public ValidationResult<PeriodCreationCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("period identifier shouldn't be null or empty");
        }else {
            if (util.aggregateGet(id, CompanyAggregate.class).isPresent()) {
                errors.add("The period with id " + id + " already exist");
            }
        }
        if (StringUtil.isNullOrEmpty(description)) {
            errors.add("Company description shouldn't be null or empty");
        }
        if(year == null) {
            errors.add("The year shouldnt be null");
        }
        return buildResult(errors);
    }
}
