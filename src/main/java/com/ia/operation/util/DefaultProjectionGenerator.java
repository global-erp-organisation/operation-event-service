package com.ia.operation.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Account;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.events.created.ProjectionCreatedEvent;
import com.ia.operation.repositories.AccountRepository;
import com.ia.operation.repositories.PeriodRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Component
public class DefaultProjectionGenerator implements ProjectionGenerator {

    private final PeriodRepository periodRepository;
    private final AccountRepository accountRepository;

    @Override
    public Flux<ProjectionCreatedEvent> generate(String year) {
        return accountRepository.findAll().flatMap(account -> {
            return generate(year,account);
        });
    }

    public  Flux<ProjectionCreatedEvent> generate(String year, Account account) {
        return periodRepository.findByYear(year).map(period -> {
            return ProjectionCreatedEvent.builder()
                    .amount(compute(account.getDefaultAmount(), account.getRecurringMode()))
                    .id(ObjectIdUtil.id())
                    .accountId(account.getId())
                    .periodId(period.getId())
                    .build();
        });
    }

    private BigDecimal compute(BigDecimal amount, RecurringMode mode) {
        switch (mode) {
            case NONE:
                return amount;
            case DAILY:
                return amount.multiply(new BigDecimal(30));
            case WEEKLY:
                return amount.multiply(new BigDecimal(4));
            case BIWEEKLY:
                return amount.multiply(new BigDecimal(2));
            case QUATERLY:
                return amount.divide(new BigDecimal(3));
            case YEARLY:
                return amount.divide(new BigDecimal(12));
            default:
                return amount;
        }
    }
}
