package com.ia.operation.util.history;

import java.util.function.Function;

import com.ia.operation.documents.Operation;

import reactor.core.publisher.Flux;

public interface HistoryUpdater<T> {
    void update(Operation event);

    default Flux<T> convert(Flux<Operation> operations, Function<Operation, T> result) {
        return operations.map(r -> {
            return result.apply(r);
        });
    }
}
