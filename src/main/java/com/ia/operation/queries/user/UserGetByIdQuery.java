package com.ia.operation.queries.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserGetByIdQuery {
    private String userId;
}
