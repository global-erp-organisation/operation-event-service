package com.ia.operation.documents.views;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonInclude(value=Include.NON_EMPTY)
public class RateView {
    private BigDecimal liquidityRatio;
    private BigDecimal billPaymentRatio;
    private BigDecimal debtRatio;
}
