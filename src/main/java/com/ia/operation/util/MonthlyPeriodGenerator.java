package com.ia.operation.util;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.axonframework.common.Assert;
import org.springframework.stereotype.Component;

import com.ia.operation.events.created.PeriodCreatedEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MonthlyPeriodGenerator implements PeriodGenerator {

    private static final int YEAR_LENGTH_IN_MONTH = 12;
    private final Locale locale;

    @Override
    public Collection<PeriodCreatedEvent> generate(String year) {
        if(!StringUtils.isNumeric(year)) {
           throw new IllegalArgumentException("The provided year should be a number."); 
        }
        return IntStream
                .range(1, YEAR_LENGTH_IN_MONTH + 1)
                .mapToObj(i -> of(Integer.valueOf(year), i))
                .map(p -> PeriodCreatedEvent.of(p, locale).build())
                .collect(Collectors.toList());
    }

    private PeriodCreatedEvent of(int year, int month) {
        final PeriodCreatedEvent.PeriodCreatedEventBuilder period = PeriodCreatedEvent.builder();
        final LocalDate start = LocalDate.of(year, 1, 1);
        final Optional<ClassOfMonth> m = ClassOfMonth.of(month);
        Assert.isTrue(m.isPresent(), () -> "Unable to find the corresponding month.");
        switch (m.get()) {
            case END_WITH_30:
                return create(year, month, 1, 30, period).build();
            case END_WITH_31:
                return create(year, month, 1, 31, period).build();
            case FEBUARY:
                if (start.isLeapYear()) {
                    return create(year, month, 1, 29, period).build();
                } else {
                    return create(year, month, 1, 28, period).build();
                }
            default:
                break;
        }
        return period.build();
    }

    private PeriodCreatedEvent.PeriodCreatedEventBuilder create(int year, int month, int startDay, int lastDay,
                                                                PeriodCreatedEvent.PeriodCreatedEventBuilder period) {
        return period
                .id(ObjectIdUtil.id())
                .end(LocalDate.of(year, month, lastDay))
                .start(LocalDate.of(year, month, startDay))
                .year(String.valueOf(year));
    }

    enum ClassOfMonth {
                       END_WITH_31(Arrays.asList(1, 3, 5, 7, 8, 10, 12)),
                       END_WITH_30(Arrays.asList(4, 6, 9, 11)),
                       FEBUARY(Arrays.asList(2));
        @Getter
        private final Collection<Integer> list;

        private ClassOfMonth(Collection<Integer> list) {
            this.list = list;
        }

        public static Optional<ClassOfMonth> of(Integer month) {
            return Optional.ofNullable(Stream.of(values()).filter(e -> e.getList().contains(month)).findFirst().orElse(null));
        }
    }
}
