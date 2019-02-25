package com.ia.operation.documents.views;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.documents.Operation;
import com.ia.operation.enums.AccountType;
import com.ia.operation.util.ObjectIdUtil;

import lombok.Builder;
import lombok.Data;

@Document
@Data
@Builder
public class YearlyHistoryView {
    private String id;
    private BigDecimal refAmount;
    private BigDecimal curAmount;
    private AccountType type;
    private int year;

    public static YearlyHistoryViewBuilder from(Operation r) {
        return YearlyHistoryView.builder()
                .id(ObjectIdUtil.id())
                .curAmount(r.getAmount())
                .refAmount(BigDecimal.ZERO)
                .year(r.getOperationDate().getYear())
                .type(r.getAccount().getAccountType());
    }
}
