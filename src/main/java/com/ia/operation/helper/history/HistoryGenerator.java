package com.ia.operation.helper.history;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Account;
import com.ia.operation.documents.Period;
import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.documents.views.MonthlyHistoryView;
import com.ia.operation.documents.views.YearlyHistoryView;
import com.ia.operation.helper.ObjectIdHelper;
import com.ia.operation.repositories.AccountRepository;
import com.ia.operation.repositories.DailyHistoryRepository;
import com.ia.operation.repositories.MonthlyHistoryRepository;
import com.ia.operation.repositories.PeriodRepository;
import com.ia.operation.repositories.YearlyHistoryRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class HistoryGenerator {

    private final DailyHistoryRepository dailyHistoryRepository;
    private final MonthlyHistoryRepository monthlyHistoryRepository;
    private final YearlyHistoryRepository yearlyHistoryRepository;
    private final PeriodRepository periodRepository;
    private final AccountRepository accountRepository;

    public void generate(Account account) {
        save(Optional.empty(), account);
    }

    public void generate(Integer year) {
        accountRepository.findAll().subscribe(a -> {
            save(Optional.ofNullable(year), a);
        });
    }

    private void save(Optional<Integer> year, Account account) {
        final Flux<Period> periods = year.isPresent() ? periodRepository.findByYear(String.valueOf(year.get())) : periodRepository.findAll();
        periods.subscribe(p -> {
            IntStream.range(0, p.getStart().getMonth().length(p.getStart().isLeapYear())).forEach(i -> {
                dailyHistoryRepository.findBydateAndAccount_id(p.getStart().plusDays(i), account.getId()).switchIfEmpty(a -> {
                    final DailyHistoryView d = DailyHistoryView.builder().id(ObjectIdHelper.id()).account(account).curAmount(BigDecimal.ZERO)
                            .refAmount(BigDecimal.ZERO).date(p.getStart().plusDays(i)).build();
                    dailyHistoryRepository.save(d).subscribe();
                }).subscribe();
            });
            monthlyHistoryRepository.findByMonthAndAccount_id(p.getDescription().toUpperCase(), account.getId()).switchIfEmpty(a -> {
                final MonthlyHistoryView m = MonthlyHistoryView.builder().id(ObjectIdHelper.id()).account(account).date(p.getStart())
                        .refAmount(BigDecimal.ZERO).curAmount(BigDecimal.ZERO).month(p.getDescription().toUpperCase()).build();
                monthlyHistoryRepository.save(m).subscribe();
            }).subscribe();
            final int yearInt = p.getStart().getYear();
            yearlyHistoryRepository.findByYearAndAccount_id(yearInt, account.getId()).switchIfEmpty(a -> {
                final YearlyHistoryView y = YearlyHistoryView.builder().id(ObjectIdHelper.id()).refAmount(BigDecimal.ZERO).curAmount(BigDecimal.ZERO)
                        .date(Year.of(yearInt).atDay(1)).year(yearInt).build();
                yearlyHistoryRepository.save(y).subscribe();
            }).subscribe();
        });

    }
}
