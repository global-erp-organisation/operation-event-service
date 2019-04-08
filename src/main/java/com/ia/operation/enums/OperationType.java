package com.ia.operation.enums;

import java.util.stream.Stream;

public enum OperationType {
    /*@formatter:off*/
   EXPENSE,
   REVENUE,
   UNDEFINED;
    /*@formatter:on*/

    public static Boolean check(OperationType type) {
        return Stream.of(values()).anyMatch(v -> v.equals(type));
    }
}
