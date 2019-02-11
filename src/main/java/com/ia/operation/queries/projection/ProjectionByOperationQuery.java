package com.ia.operation.queries.projection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectionByOperationQuery {
    private String operationId;
    private String userId;
}
