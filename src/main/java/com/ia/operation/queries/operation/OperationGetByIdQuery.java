package com.ia.operation.queries.operation;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OperationGetByIdQuery {
    private String operationId;
}
