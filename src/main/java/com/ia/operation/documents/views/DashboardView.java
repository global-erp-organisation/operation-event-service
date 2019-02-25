package com.ia.operation.documents.views;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DashboardView {
    private List<DailyHistoryView> dailyHistory;
    private List<MonthlyHistoryView> monthlyHistory;
    private List<YearlyHistoryView> yearlyHistory;
}
