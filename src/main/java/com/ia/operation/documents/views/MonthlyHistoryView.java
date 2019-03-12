package com.ia.operation.documents.views;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.documents.Account;
import com.ia.operation.documents.Operation;
import com.ia.operation.util.ObjectIdUtil;

import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
@JsonInclude(value=Include.NON_EMPTY)
public class MonthlyHistoryView {
    private String id;
    @JsonProperty("ref_amount")
    private BigDecimal refAmount;
    @JsonProperty("cur_amount")
    private BigDecimal curAmount;
    private Account account;
    private String month;
    private LocalDate date;

    
    public static MonthlyHistoryViewBuilder from (Operation r) {
        return MonthlyHistoryView.builder()
                .id(ObjectIdUtil.id())
                .curAmount(r.getAmount())
                .account(r.getAccount())
                .refAmount(BigDecimal.ZERO)
                .month(r.getPeriod().getDescription().toUpperCase())
                .date(r.getOperationDate());
    }
}
