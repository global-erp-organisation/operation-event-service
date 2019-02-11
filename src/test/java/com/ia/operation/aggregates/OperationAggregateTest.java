package com.ia.operation.aggregates;

import java.math.BigDecimal;

import org.axonframework.eventsourcing.eventstore.EventStoreException;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Test;

import com.ia.operation.commands.creation.OperationCreationCmd;
import com.ia.operation.enums.OperationType;
import com.ia.operation.enums.RecurringMode;
import com.ia.operation.util.TestUtil;

public class OperationAggregateTest {

    private final FixtureConfiguration<OperationAggregate> fixture = TestUtil.fixture(OperationAggregate.class);

    @Test
    public void should_emit_a_operationCreatedEvent_when_a_operationCreationCmd_is_apply() {
        final OperationCreationCmd cmd = OperationCreationCmd.builder().id("id").defaultAmount(BigDecimal.ZERO).description("description")
                .operationType(OperationType.EXPENSE).userId("userId").recurringMode(RecurringMode.MONTHLY).build();
        fixture.given().when(cmd).expectSuccessfulHandlerExecution().expectEvents(OperationCreationCmd.of(cmd));
    }

    @Test
    public void should_throw_an_eventStorageExeption_when_create_an_operation_with_the_same_id() {
        final OperationCreationCmd cmd = OperationCreationCmd.builder().id("id").defaultAmount(BigDecimal.ZERO).description("description")
                .operationType(OperationType.EXPENSE).userId("userId").recurringMode(RecurringMode.MONTHLY).build();
        final OperationCreationCmd other = OperationCreationCmd.builder().id("id").defaultAmount(BigDecimal.ZERO).description("description")
                .operationType(OperationType.REVENUE).userId("userId").recurringMode(RecurringMode.MONTHLY).build();

        fixture.given(OperationCreationCmd.of(cmd)).when(other).expectException(EventStoreException.class).expectEvents(OperationCreationCmd.of(other));
    }
}
