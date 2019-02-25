package com.ia.operation.events.created;


import java.math.BigDecimal;

import com.ia.operation.enums.AccountType;
import com.ia.operation.enums.RecurringMode;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountCreatedEvent {
    private String id;
    private String description;
    private String userId;
    private AccountType accountType;
    private RecurringMode recurringMode;
    private BigDecimal defaultAmount;
    private String categoryId;
}
