package com.ia.operation.documents;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.enums.AccountType;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.events.created.AccountCreatedEvent;
import com.ia.operation.events.updated.AccountUpdatedEvent;

import io.github.kaiso.relmongo.annotation.FetchType;
import io.github.kaiso.relmongo.annotation.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
@JsonInclude(value = Include.NON_EMPTY)
public class Account {
    @Id
    private String id;
    private String description;
    @JsonProperty("account_type")
    private AccountType accountType;
    @JsonProperty("recurring_mode")
    private RecurringMode recurringMode;
    @JsonProperty("default_amount")
    private BigDecimal defaultAmount;
    @ManyToOne(fetch = FetchType.EAGER)
    private AccountCategory category;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public static Account of(AccountCreatedEvent event, User user, AccountCategory category) {
        return Account.builder().id(event.getId()).user(user).description(event.getDescription()).recurringMode(event.getRecurringMode())
                .accountType(event.getAccountType()).defaultAmount(event.getDefaultAmount()).category(category).build();
    }

    public static Account of(AccountUpdatedEvent event, User user, AccountCategory category) {
        return Account.builder().id(event.getId()).user(user).description(event.getDescription()).recurringMode(event.getRecurringMode())
                .accountType(event.getAccountType()).defaultAmount(event.getDefaultAmount()).category(category).build();
    }
}
