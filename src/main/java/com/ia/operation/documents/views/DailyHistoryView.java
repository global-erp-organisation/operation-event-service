package com.ia.operation.documents.views;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.documents.Operation;
import com.ia.operation.enums.AccountType;

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
    private AccountType type;
    private LocalDate date;
    
    public static DailyHistoryViewBuilder from (Operation r) {
        return DailyHistoryView.builder()
                .date(r.getOperationDate())
                .refAmount(BigDecimal.ZERO)
                .type(r.getAccount().getAccountType())
                .curAmount(r.getAmount());
    }
    
    public static DailyHistoryViewBuilder from (DailyHistoryView r) {
        return DailyHistoryView.builder()
                .id(r.getId())
                .date(r.getDate())
                .type(r.getType())
                .refAmount(r.getRefAmount())
                .curAmount(r.getCurAmount());
    }
}
