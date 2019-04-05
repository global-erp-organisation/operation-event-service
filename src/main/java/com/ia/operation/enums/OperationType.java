package com.ia.operation.enums;

import java.util.stream.Stream;

public enum OperationType {
   EXPENSE,
   REVENUE,
   UNDEFINED;
    
    
    public static Boolean check(OperationType type) {
        return Stream.of(values()).anyMatch(v -> v.equals(type));
    }
}
