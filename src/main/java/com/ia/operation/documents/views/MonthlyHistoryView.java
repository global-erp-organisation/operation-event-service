package com.ia.operation.documents.views;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ia.operation.documents.Operation;
import com.ia.operation.helper.ObjectIdHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Document
@SuperBuilder
@Data
@JsonInclude(value = Include.NON_EMPTY)
public class MonthlyHistoryView extends DailyHistoryView {
    private String month;
    public static MonthlyHistoryViewBuilder from(Operation r) {
        return MonthlyHistoryView.builder()
                .id(ObjectIdHelper.id())
                .curAmount(r.getAmount())
                .account(r.getAccount())
                .refAmount(BigDecimal.ZERO)
                .month(r.getPeriod().getDescription().toUpperCase())
                .date(r.getOperationDate());
    }
}
