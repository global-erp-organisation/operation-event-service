package com.ia.operation.queries.account;

import java.time.LocalDate;

import com.ia.operation.documents.views.DashboardView;
import com.ia.operation.documents.views.DashboardView.DashboardParams;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DashboardQuery {
    private LocalDate start;
    private LocalDate end;
    private String userId;
    private Boolean daily;
    private Boolean monthly;
    private Boolean yearly;

    public static DashboardView.DashboardParams from(DashboardQuery q) {
        return DashboardParams.builder()
                .start(q.getStart())
                .end(q.getEnd())
                .userId(q.getUserId())
                .daily(q.getDaily())
                .monthly(q.getMonthly())
                .yearly(q.getYearly())
                .build();
    }
}
