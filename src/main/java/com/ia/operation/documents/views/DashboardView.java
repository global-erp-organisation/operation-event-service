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
    History history;

    @Value
    @Builder
    public static class DashboardParams {
        LocalDate start;
        LocalDate end;
        @JsonProperty("user_id")
        String userId;
        Boolean daily;
        Boolean monthly;
        Boolean yearly;
    }

    @Value
    @Builder
    @JsonInclude(value = Include.NON_EMPTY)
    public static class History {
        LocalDate start;
        LocalDate end;
        @JsonProperty("daily_history")
        HistoryView<DailyHistoryView> dailyHistory;
        @JsonProperty("monthly_history")
        HistoryView<MonthlyHistoryView> monthlyHistory;
        @JsonProperty("yearly_history")
        HistoryView<YearlyHistoryView> yearlyHistory;
        @JsonProperty("rate_view")
        RateView rateView;
    }
}
