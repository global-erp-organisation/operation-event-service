package com.ia.operation.enums;

import java.util.stream.Stream;

public enum AccountType {
    /*@formatter:off*/
    BANKING,
    OTHER;
    /*@formatter:on*/
    public static boolean check(AccountType accountType) {
        return Stream.of(values()).anyMatch(v -> v.equals(accountType));
    }
}
