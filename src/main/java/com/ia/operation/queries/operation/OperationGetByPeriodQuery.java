package com.ia.operation.queries.operation;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OperationGetByPeriodQuery {
    String userId;
    String periodId;
}
