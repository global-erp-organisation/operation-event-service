package com.ia.operation.documents.views;

import java.math.BigDecimal;
import java.time.Month;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.documents.Account;
import com.ia.operation.documents.Operation;
import com.ia.operation.util.ObjectIdUtil;

import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
public class MonthlyHistoryView {
    private String id;
    private BigDecimal refAmount;
    private BigDecimal curAmount;
    private Account account;
    private String month;
    private Month key;
    
    public static MonthlyHistoryViewBuilder from (Operation r) {
        return MonthlyHistoryView.builder()
                .id(ObjectIdUtil.id())
                .curAmount(r.getAmount())
                .account(r.getAccount())
                .refAmount(BigDecimal.ZERO)
                .month(r.getPeriod().getDescription().toUpperCase())
                .key(r.getOperationDate().getMonth());
    }
}
