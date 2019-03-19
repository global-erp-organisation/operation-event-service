package com.ia.operation.queries.projection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectionByAccountQuery {
    private String accountId;
    private String year;
}
