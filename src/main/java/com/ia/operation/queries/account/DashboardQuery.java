package com.ia.operation.queries.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ia.operation.documents.views.DashboardView;
import com.ia.operation.documents.views.DashboardView.DashboardParams;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class DashboardQuery {
    LocalDate start;
    LocalDate end;
    String userId;
    Boolean daily;
    Boolean monthly;
    Boolean yearly;

    @JsonIgnore
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
