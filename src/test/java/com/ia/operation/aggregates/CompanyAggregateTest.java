package com.ia.operation.aggregates;

import java.util.HashMap;

import org.axonframework.eventsourcing.IncompatibleAggregateException;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Test;

import com.ia.operation.commands.creation.CompanyCreationCmd;
import com.ia.operation.util.TestUtil;

public class CompanyAggregateTest {

    private final FixtureConfiguration<CompanyAggregate> fixture = TestUtil.fixture(CompanyAggregate.class);

    @Test
    public void should_emit_companyCreationEvent_when_a_companyCreationCmd_is_apply() {
        final CompanyCreationCmd cmd = CompanyCreationCmd.builder().id("id").description("description").details(new HashMap<>()).build();
        fixture.given().when(cmd).expectSuccessfulHandlerExecution().expectEvents(CompanyCreationCmd.eventFrom(cmd).build());
    }

    @Test
    public void should_throw_an_incompatibleAggregateException_when_companyId_is_null_or_empty() {
        final CompanyCreationCmd cmd = CompanyCreationCmd.builder().description("description").details(new HashMap<>()).build();
        fixture.given().when(cmd).expectException(IncompatibleAggregateException.class);
    }

}
