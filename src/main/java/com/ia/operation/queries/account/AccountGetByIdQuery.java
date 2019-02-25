package com.ia.operation.queries.account;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountGetByIdQuery {
    private String accountId;
}
