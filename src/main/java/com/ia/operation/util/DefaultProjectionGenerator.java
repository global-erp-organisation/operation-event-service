package com.ia.operation.util;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.ia.operation.configuration.AxonProperties;
import com.ia.operation.documents.Account;
import com.ia.operation.documents.Period;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.events.created.ProjectionCreatedEvent;
import com.ia.operation.repositories.AccountRepository;
import com.ia.operation.repositories.PeriodRepository;
import com.ia.operation.repositories.ProjectionRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Component
public class DefaultProjectionGenerator implements ProjectionGenerator {

    private final PeriodRepository periodRepository;
    private final AccountRepository accountRepository;
    private final ProjectionRepository projectionRepository;
    private final RabbitTemplate rabbitTemplate;
    private final AxonProperties properties;

    @Override
    public void generate(String year) {
        accountRepository.findAll().subscribe(account -> {
            generate(Optional.ofNullable(year), account);
        });
    }

    @Override
    public void generate(Period period) {
        accountRepository.findAll().subscribe(account -> {
            createAndSendProjection(period, account);
        });
    }

    @Override
    public void generate(Optional<String> year, Account account) {
        final Flux<Period> periods =
                year.isPresent() ? periodRepository.findByYear(year.get())
                                 : periodRepository.findTop1ByOrderByStartDesc().flatMap(p -> periodRepository.findByYear(p.getYear()));

        periods.subscribe(period -> projectionRepository.findByAccount_IdAndPeriod_Id(account.getId(), period.getId())
                .switchIfEmpty(a -> createAndSendProjection(period, account)).collectList().subscribe());
    }

    private void createAndSendProjection(Period period, Account account) {
        final ProjectionCreatedEvent projection = ProjectionCreatedEvent.builder().amount(compute(account.getDefaultAmount(), account.getRecurringMode()))
                .id(ObjectIdUtil.id()).accountId(account.getId()).periodId(period.getId()).build();
        rabbitTemplate.convertAndSend(properties.getDefaultEventRoutingKey(), projection);
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
