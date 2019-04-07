package com.ia.operation.helper.history.ratios;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;
import reactor.core.publisher.Mono;

public interface RatioBuilder {
    
    Mono<BigDecimal> build(RatioParams params);
    
    @Value
    @Builder
    public static class RatioParams{
        private final LocalDate start;
        private final LocalDate end;
        private final String userId;
    }
}
