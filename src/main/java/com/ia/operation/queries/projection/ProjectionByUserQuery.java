package com.ia.operation.queries.projection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectionByUserQuery {
    private String userId;
}
