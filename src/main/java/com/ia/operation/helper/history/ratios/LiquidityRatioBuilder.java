package com.ia.operation.helper.history.ratios;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Account;
import com.ia.operation.documents.Projection;
import com.ia.operation.documents.views.RateView;
import com.ia.operation.enums.AccountType;
import com.ia.operation.enums.OperationType;
import com.ia.operation.repositories.AccountRepository;
import com.ia.operation.repositories.ProjectionRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class LiquidityRatioBuilder implements RatioBuilder {
    private final ProjectionRepository projectionRepository;
    private final AccountRepository accountRepository;

    @Override
    public Mono<RateView> build(RatioParams params) {
        final Mono<List<Account>> bankingAccounts = accountRepository.findByUser_idAndAccountType(params.getUserId(), AccountType.BANKING).collectList();
        final Mono<List<Projection>> projections =
                projectionRepository.findByUserAndPeriod(params.getUserId(), params.getStart(), params.getEnd(), OperationType.EXPENSE).collectList();
        return bankingAccounts.flatMap(accounts -> {
            return projections.map(pr -> {
                final double balance = accounts.stream().mapToDouble(a -> a.getBalance().doubleValue()).sum();
                final double projection = pr.stream().mapToDouble(p -> p.getAmount().doubleValue()).sum();
                return RateView.builder().liquidityRatio(new BigDecimal(balance / projection)).build();
            });
        });
    }

}
