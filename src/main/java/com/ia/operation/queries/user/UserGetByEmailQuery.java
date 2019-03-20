package com.ia.operation.queries.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserGetByEmailQuery {
    private String email;
}
