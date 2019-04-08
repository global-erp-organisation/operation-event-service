package com.ia.operation.helper.history.ratios;

import java.time.LocalDate;

import com.ia.operation.documents.views.RateView;

import lombok.Builder;
import lombok.Value;
import reactor.core.publisher.Mono;

public interface RatioBuilder {
    
    Mono<RateView> build(RatioParams params);
    
    @Value
    @Builder
    public static class RatioParams{
        private final LocalDate start;
        private final LocalDate end;
        private final String userId;
    }
}
