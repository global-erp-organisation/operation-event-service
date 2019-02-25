package com.ia.operation.enums;

import java.util.stream.Stream;

public enum AccountType {
   EXPENSE,
   REVENUE,
   UNDEFINED;
    
    
    public static Boolean check(AccountType type) {
        return Stream.of(values()).anyMatch(v -> v.equals(type));
    }
}
