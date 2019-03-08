package com.ia.operation.documents.views;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonInclude(value = Include.NON_NULL)
public class DashboardView {

    private HistoryView<DailyHistoryView> dailyHistory;
    private HistoryView<MonthlyHistoryView> monthlyHistory;
    private HistoryView<YearlyHistoryView> yearlyHistory;

    @Value
    @Builder
    public static class DashboardParams {
        private LocalDate start;
        private LocalDate end;
        private String userId;
    }
}
