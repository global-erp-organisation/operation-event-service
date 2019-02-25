package com.ia.operation.queries.account;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DashboardQuery {
    private LocalDate start;
    private LocalDate end;
}
