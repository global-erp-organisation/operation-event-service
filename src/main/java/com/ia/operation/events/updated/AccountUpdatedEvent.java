package com.ia.operation.events.updated;

import java.math.BigDecimal;

import com.ia.operation.enums.AccountType;
import com.ia.operation.enums.RecurringMode;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountUpdatedEvent {
    private String id;
    private String description;
    private AccountType accountType;
    private RecurringMode recurringMode;
    private BigDecimal defaultAmount;
    private String userId;
    private String categoryId;
}
