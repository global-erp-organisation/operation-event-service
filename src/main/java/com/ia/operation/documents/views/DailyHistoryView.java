package com.ia.operation.documents.views;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.documents.Account;
import com.ia.operation.documents.Operation;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Document
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class DailyHistoryView {
    @Id
    private String id;
    private BigDecimal refAmount;
    private BigDecimal curAmount;
    private Account account;
    private LocalDate date;
    
    public static DailyHistoryViewBuilder from (Operation r) {
        return DailyHistoryView.builder()
                .date(r.getOperationDate())
                .refAmount(BigDecimal.ZERO)
                .account(r.getAccount())
                .curAmount(r.getAmount());
    }
    
    public static DailyHistoryViewBuilder from (DailyHistoryView r) {
        return DailyHistoryView.builder()
                .id(r.getId())
                .date(r.getDate())
                .account(r.getAccount())
                .refAmount(r.getRefAmount())
                .curAmount(r.getCurAmount());
    }
}
