package com.ia.operation.queries.period;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PeriodGetByYear {
    private int year;
}
