package com.ia.operation.commands.creation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ia.operation.aggregates.CompanyAggregate;
import com.ia.operation.events.created.PeriodCreatedEvent;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.validator.CommandValidator;
import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class PeriodCreationCmd extends CommandValidator<PeriodCreationCmd> {
    @TargetAggregateIdentifier
    protected String id;
     String year;
     String description;
     LocalDate start;
     LocalDate end;
    @Builder.Default
     boolean close = false;
    @JsonIgnore
     PeriodCreatedEvent event;

    public static PeriodCreatedEvent.PeriodCreatedEventBuilder eventFrom(PeriodCreationCmd cmd) {
        return PeriodCreatedEvent.builder()
                .year(cmd.getYear())
                .id(cmd.getId())
                .description(cmd.getDescription())
                .start(cmd.getStart())
                .end(cmd.getEnd())
                .close(cmd.isClose());
    }

    public static PeriodCreationCmdBuilder cmdFrom(PeriodCreationCmd cmd) {
        return PeriodCreationCmd.builder()
                .year(cmd.getYear())
                .description(cmd.getDescription())
                .id(cmd.getId())
                .start(cmd.getStart())
                .end(cmd.getEnd())
                 .close(cmd.isClose());
    }

    public static PeriodCreationCmdBuilder cmdFrom(PeriodCreatedEvent event) {
        return PeriodCreationCmd.builder()
                .id(event.getId())
                .year(event.getYear())
                .description(event.getDescription())
                .start(event.getStart())
                .end(event.getEnd())
                .event(event)
                .close(event.isClose());
    }

    @Override
    public ValidationResult<PeriodCreationCmd> validate(AggregateHelper util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtil.isNullOrEmpty(id)) {
            errors.add("period identifier shouldn't be null or empty");
        } else {
            if (util.aggregateGet(id, CompanyAggregate.class).isPresent()) {
                errors.add("The period with id " + id + " already exist");
            }
        }
        if (StringUtil.isNullOrEmpty(description)) {
            errors.add("Company description shouldn't be null or empty");
        }
        if (year == null) {
            errors.add("The year shouldnt be null");
        }
        return buildResult(errors);
    }
}
