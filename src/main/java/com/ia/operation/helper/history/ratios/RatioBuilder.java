package com.ia.operation.helper.history.ratios;

import com.ia.operation.documents.views.RateView;
import lombok.Builder;
import lombok.Value;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface RatioBuilder {

    Mono<RateView> build(RatioParams params);

    @Value
    @Builder
    class RatioParams {
        LocalDate start;
        LocalDate end;
        String userId;
    }
}
