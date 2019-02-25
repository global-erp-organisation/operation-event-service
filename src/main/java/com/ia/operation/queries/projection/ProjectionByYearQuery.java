package com.ia.operation.queries.projection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectionByYearQuery {
    private String year;
    private String userId;
}
