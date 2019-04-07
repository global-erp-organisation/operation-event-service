package com.ia.operation.helper.history;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.documents.views.DashboardView;
import com.ia.operation.documents.views.DashboardView.History;
import com.ia.operation.documents.views.HistoryView;
import com.ia.operation.documents.views.MonthlyHistoryView;
import com.ia.operation.documents.views.YearlyHistoryView;
import com.ia.operation.repositories.DailyHistoryRepository;
import com.ia.operation.repositories.HistoryQuery;
import com.ia.operation.repositories.MonthlyHistoryRepository;
import com.ia.operation.repositories.YearlyHistoryRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DashboardBuilder {
    private final DailyHistoryRepository dailyHistoryRepository;
    private final MonthlyHistoryRepository monthlyHistoryRepository;
    private final YearlyHistoryRepository yearlyHistoryRepository;

    public Mono<DashboardView> build(DashboardView.DashboardParams param) {
        
        final Flux<DailyHistoryView> dh = param.getDaily() ? query(param, dailyHistoryRepository) : Flux.just(DailyHistoryView.builder().build());
        final Flux<MonthlyHistoryView> mh = param.getMonthly() ? query(param, monthlyHistoryRepository) : Flux.just(MonthlyHistoryView.builder().build());
        final Flux<YearlyHistoryView> yh = param.getYearly() ? query(param, yearlyHistoryRepository) : Flux.just(YearlyHistoryView.builder().build());

        return dh.collectList().flatMap(d -> {
            return mh.collectList().flatMap(m -> {
                return yh.collectList().map(y -> {
                    final History history = History.builder().start(param.getStart()).end(param.getEnd())
                            .dailyHistory(param.getDaily() ? HistoryView.<DailyHistoryView>builder().details(d).build() : null)
                            .monthlyHistory(param.getMonthly() ? HistoryView.<MonthlyHistoryView>builder().details(m).build() : null)
                            .yearlyHistory(param.getYearly() ? HistoryView.<YearlyHistoryView>builder().details(y).build() : null).build();
                    return DashboardView.builder().history(history).build();
                });
            });
        });
    }

    private <T> Flux<T> query(DashboardView.DashboardParams param, HistoryQuery<T> repository) {
        return repository.findByAccount_User_IdAndDateBetweenOrderByDate(param.getUserId(), param.getStart().minusDays(1), param.getEnd().plusDays(1));
    }
}
