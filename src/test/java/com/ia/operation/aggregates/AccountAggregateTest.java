package com.ia.operation.aggregates;

import java.math.BigDecimal;

import org.axonframework.eventsourcing.eventstore.EventStoreException;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Ignore;
import org.junit.Test;

import com.ia.operation.commands.creation.AccountCreationCmd;
import com.ia.operation.enums.AccountType;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.util.TestUtil;

public class AccountAggregateTest {

    private final FixtureConfiguration<AccountAggregate> fixture = TestUtil.fixture(AccountAggregate.class);

    @Test
    @Ignore
    public void should_emit_a_operationCreatedEvent_when_a_operationCreationCmd_is_apply() {
        final AccountCreationCmd cmd = AccountCreationCmd.builder().id("id").defaultAmount(BigDecimal.ZERO).description("description")
                .accountType(AccountType.EXPENSE).userId("userId").recurringMode(RecurringMode.MONTHLY).build();
        fixture.given().when(cmd).expectSuccessfulHandlerExecution().expectEvents(AccountCreationCmd.enventFrom(cmd));
    }

    @Test
    @Ignore
    public void should_throw_an_eventStorageExeption_when_create_an_operation_with_the_same_id() {
        final AccountCreationCmd cmd = AccountCreationCmd.builder().id("id").defaultAmount(BigDecimal.ZERO).description("description")
                .accountType(AccountType.EXPENSE).userId("userId").recurringMode(RecurringMode.MONTHLY).build();
        final AccountCreationCmd other = AccountCreationCmd.builder().id("id").defaultAmount(BigDecimal.ZERO).description("description")
                .accountType(AccountType.REVENUE).userId("userId").recurringMode(RecurringMode.MONTHLY).build();

        fixture.given(AccountCreationCmd.enventFrom(cmd)).when(other).expectException(EventStoreException.class).expectEvents(AccountCreationCmd.enventFrom(other));
    }
}
