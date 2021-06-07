package com.ia.operation.queries.account;

import com.ia.operation.documents.views.DashboardView;
import com.ia.operation.documents.views.DashboardView.DashboardParams;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class DashboardQuery {
    LocalDate start;
    LocalDate end;
    String userId;
    Boolean daily;
    Boolean monthly;
    Boolean yearly;

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
