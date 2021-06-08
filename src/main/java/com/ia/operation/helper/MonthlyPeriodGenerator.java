package com.ia.operation.helper;

import com.ia.operation.documents.Period;
import com.ia.operation.events.created.PeriodCreatedEvent;
import com.ia.operation.repositories.PeriodRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class MonthlyPeriodGenerator implements PeriodGenerator {

    public static final int FIRST_DAY_OF_MONTH = 1;
    private static final int YEAR_LENGTH_IN_MONTH = 12;
    private final Locale locale;
    private final PeriodRepository periodRepository;

    @Override
    public Collection<PeriodCreatedEvent> generate(String year) {
        if (!StringUtils.isNumeric(year)) {
            throw new IllegalArgumentException("The provided year should be a number.");
        }
        return IntStream.range(1, YEAR_LENGTH_IN_MONTH + 1).mapToObj(i -> of(Integer.parseInt(year), i)).map(p -> PeriodCreatedEvent.of(p, locale).build())
        .collect(Collectors.toList());
    }

    private PeriodCreatedEvent of(int year, int month) {
        final PeriodCreatedEvent.PeriodCreatedEventBuilder period = PeriodCreatedEvent.builder();
        final LocalDate start = LocalDate.of(year, month, FIRST_DAY_OF_MONTH);
        final YearMonth ym = YearMonth.of(year, month);
        final LocalDate end = LocalDate.of(year, month, ym.lengthOfMonth());
        final String description = String.format("%s-%s", start.getMonth().getDisplayName(TextStyle.FULL, locale), year);
        return period.id(ObjectIdHelper.id()).description(description).start(start).end(end).build();
    }

    @Override
    public Mono<Optional<Period>> resolve(LocalDate date, String year) {
        final Mono<List<Period>> periods = periodRepository.findByYear(year).collectList();
        return periods.map(p -> {
            return resolve(date, p);
        });
    }
}
