package com.ia.operation.documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.enums.AccountType;
import com.ia.operation.enums.OperationType;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.events.created.AccountCreatedEvent;
import com.ia.operation.events.updated.AccountUpdatedEvent;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
@Builder
@Data
@JsonInclude(value = Include.NON_EMPTY)
public class Account {
    @Id
    private String id;
    private String description;
    @JsonProperty("recurring_mode")
    private RecurringMode recurringMode;
    @JsonProperty("default_amount")
    private BigDecimal defaultAmount;
    //@ManyToOne(fetch = FetchType.EAGER)
    private AccountCategory category;
    //@ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;
    private BigDecimal balance;
    @JsonProperty("account_type")
    private AccountType accountType;
    @JsonProperty("default_operation_type")
    private OperationType defaultOperationType;

    public static Account of(AccountCreatedEvent event, User user, AccountCategory category) {
        /*@formatter:off*/
        return Account.builder()
                .id(event.getId())
                .user(user)
                .description(event.getDescription())
                .recurringMode(event.getRecurringMode())
                .defaultAmount(event.getDefaultAmount())
                .category(category)
                .accountType(event.getAccountType())
                .balance(event.getBalance())
                .defaultOperationType(event.getDefaultOperationType())
                .build();
        /*@formatter:on*/
    }

    public static Account of(AccountUpdatedEvent event, User user, AccountCategory category) {
        /*@formatter:off*/
        return Account.builder()
                .id(event.getId())
                .user(user)
                .description(event.getDescription())
                .recurringMode(event.getRecurringMode())
                .defaultAmount(event.getDefaultAmount())
                .category(category)
                .accountType(event.getAccountType())
                .balance(event.getBalance())
                .defaultOperationType(event.getDefaultOperationType())
                .build();
        /*@formatter:on*/
    }
}
