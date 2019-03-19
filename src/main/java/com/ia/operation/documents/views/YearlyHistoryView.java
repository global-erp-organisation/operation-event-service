package com.ia.operation.documents.views;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.documents.Account;
import com.ia.operation.documents.Operation;
import com.ia.operation.util.ObjectIdUtil;

import io.github.kaiso.relmongo.annotation.FetchType;
import io.github.kaiso.relmongo.annotation.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Document
@Data
@Builder
public class YearlyHistoryView {
    private String id;
    @JsonProperty("ref_amount")
    private BigDecimal refAmount;
    @JsonProperty("cur_amount")
    private BigDecimal curAmount;
    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;
    private int year;
    private LocalDate date;


    public static YearlyHistoryViewBuilder from(Operation r) {
        return YearlyHistoryView.builder()
                .id(ObjectIdUtil.id())
                .curAmount(r.getAmount())
                .refAmount(BigDecimal.ZERO)
                .year(r.getOperationDate().getYear())
                .date(r.getOperationDate())
                .account(r.getAccount());
    }
}
