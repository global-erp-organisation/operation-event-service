package com.ia.operation.documents.views;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonInclude(value = Include.NON_EMPTY)
public class DashboardView {
    private History history;

    @Value
    @Builder
    public static class DashboardParams {
        private LocalDate start;
        private LocalDate end;
        @JsonProperty("user_id")
        private String userId;
        private Boolean daily;
        private Boolean monthly;
        private Boolean yearly;
    }

    @Value
    @Builder
    @JsonInclude(value = Include.NON_EMPTY)
    public static class History {
        private LocalDate start;
        private LocalDate end;
        @JsonProperty("daily_history")
        private HistoryView<DailyHistoryView> dailyHistory;
        @JsonProperty("monthly_history")
        private HistoryView<MonthlyHistoryView> monthlyHistory;
        @JsonProperty("yearly_history")
        private HistoryView<YearlyHistoryView> yearlyHistory;
        @JsonProperty("rate_view")
        private RateView rateView;
    }
}
