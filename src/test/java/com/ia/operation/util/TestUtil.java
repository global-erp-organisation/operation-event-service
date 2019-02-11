package com.ia.operation.util;

import java.util.List;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtil {

    public static <T> FixtureConfiguration<T> fixture(Class<T> type) {
        return new AggregateTestFixture<T>(type);
    }

    public static <T> void verify(FixtureConfiguration<T> fixture, Object cmd, List<Object> expectEvents, Object... pastEvents) {
        fixture.given(pastEvents).when(cmd).expectEvents(expectEvents.toArray());
    }
}
