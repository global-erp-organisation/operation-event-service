package com.ia.operation.util.history;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.documents.views.DashboardView;
import com.ia.operation.documents.views.HistoryView;
import com.ia.operation.repositories.DailyHistoryRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DashboardBuilder {
    private final DailyHistoryRepository dailyHistoryRepository;

    public Mono<DashboardView> build(DashboardView.DashboardParams param) {
        final Flux<DailyHistoryView> dh =
                dailyHistoryRepository.findByAccount_User_IdAndDateBetween(param.getUserId(), param.getStart().minusDays(1), param.getEnd().plusDays(1));
        return dh.collectList().map(d -> {
            return DashboardView.builder()
                    .dailyHistory(HistoryView.<DailyHistoryView>builder().start(param.getStart()).end(param.getEnd()).details(d).build()).build();
        });
    }
}
