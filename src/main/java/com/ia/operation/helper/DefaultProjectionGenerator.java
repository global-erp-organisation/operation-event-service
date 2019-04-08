package com.ia.operation.helper;

import java.math.BigDecimal;
import java.math.MathContext;
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

    private static final int TWELVE_FACTOR = 12;
    private static final int THREE_FACTOR = 3;
    private static final int TWO_FACTOR = 2;
    private static final int FOUR_FACTOR = 4;
    private static final int THIRTY_FACTOR = 30;

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
                                 : periodRepository.findTop1ByOrderByStartDesc()
                                 .flatMap(p -> periodRepository.findByYear(p.getYear()));

        periods.subscribe(period -> {
            projectionRepository.findByAccount_IdAndPeriod_IdOrderByAccount_description(account.getId(), period.getId())
            .switchIfEmpty(a -> {
                createAndSendProjection(period, account);
            }).subscribe();
        });
    }

    private void createAndSendProjection(Period period, Account account) {
        /*@formatter:off*/
        final ProjectionCreatedEvent projection = ProjectionCreatedEvent.builder()
                .amount(compute(account.getDefaultAmount(), account.getRecurringMode()))
                .id(ObjectIdHelper.id())
                .accountId(account.getId())
                .periodId(period.getId())
                .operationType(account.getDefaultOperationType())
                .build();
        /*@formatter:on*/
        rabbitTemplate.convertAndSend(properties.getDefaultEventRoutingKey(), projection);
    }

    private BigDecimal compute(BigDecimal amount, RecurringMode mode) {
        switch (mode) {
            case NONE:
                return amount;
            case DAILY:
                return amount.multiply(new BigDecimal(THIRTY_FACTOR));
            case WEEKLY:
                return amount.multiply(new BigDecimal(FOUR_FACTOR));
            case BIWEEKLY:
                return amount.multiply(new BigDecimal(TWO_FACTOR));
            case QUATERLY:
                return new BigDecimal(amount.doubleValue() / THREE_FACTOR, MathContext.DECIMAL32);
            case YEARLY:
                return new BigDecimal(amount.doubleValue() / TWELVE_FACTOR, MathContext.DECIMAL32);
            default:
                return amount;
        }
    }
}
