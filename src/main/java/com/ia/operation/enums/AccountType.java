package com.ia.operation.enums;

import java.util.stream.Stream;

public enum AccountType {
    /*@formatter:off*/
    TREASURY,
    THRID_PARTY,
    EXPENSE,
    REVENUE,
    OTHER;
    /*@formatter:on*/
    
    /**
     * Check if the provided value belongs the account type enum domain value
     * 
     * @param accountType
     * @return
     */
    public static boolean check(AccountType accountType) {
        return Stream.of(values()).anyMatch(v -> v.equals(accountType));
    }
}
