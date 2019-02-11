package com.ia.operation.queries.projection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectionByPeriodQuery {
    private String periodId;
    private String userId;
}
