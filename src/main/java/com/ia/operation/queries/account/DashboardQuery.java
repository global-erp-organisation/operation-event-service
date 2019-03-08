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

    public static DashboardView.DashboardParams from(DashboardQuery q) {
        return DashboardParams.builder().start(q.getStart()).end(q.getEnd()).userId(q.getUserId()).build();
    }
}
