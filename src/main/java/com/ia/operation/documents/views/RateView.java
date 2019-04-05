package com.ia.operation.documents.views;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RateView {
    private BigDecimal liquidityRatio;
    private BigDecimal billPaymentRatio;
    private BigDecimal debtRatio;
}
