package com.ia.operation.util.history;

import java.util.function.Function;

import com.ia.operation.documents.Realisation;

import reactor.core.publisher.Flux;

public interface HistoryUpdater<T> {
    void update(Realisation event);

    default Flux<T> convert(Flux<Realisation> realisations, Function<Realisation, T> result) {
        return realisations.map(r -> {
            return result.apply(r);
        });
    }
}
