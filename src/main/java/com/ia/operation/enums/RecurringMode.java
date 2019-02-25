package com.ia.operation.enums;

import java.util.stream.Stream;

public enum RecurringMode {
 DAILY,
 WEEKLY,
 BIWEEKLY,
 MONTHLY,
 QUATERLY,
 YEARLY,
 NONE;
    
    /**
     * Check if the provided value belongs the ReccuringMode enum domain value
     * @param mode
     * @return
     */
    public static Boolean check(RecurringMode mode) {
        return Stream.of(values()).anyMatch(v -> v.equals(mode));
    }
}
