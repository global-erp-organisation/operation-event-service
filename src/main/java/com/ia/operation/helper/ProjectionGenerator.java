package com.ia.operation.helper;

import java.util.Optional;

import com.ia.operation.documents.Account;
import com.ia.operation.documents.Period;

public interface ProjectionGenerator {
    void generate(String year);
    void generate(Period period);
    void generate(Optional<String> year, Account account);

    default void generate(Account account) {
        generate(Optional.empty(), account);
    }
}
