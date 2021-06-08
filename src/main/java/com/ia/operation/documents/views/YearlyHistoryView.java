package com.ia.operation.documents.views;

import com.ia.operation.documents.Operation;
import com.ia.operation.helper.ObjectIdHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Document
@Data
public class YearlyHistoryView extends DailyHistoryView {
    private int year;

    public static YearlyHistoryViewBuilder from(Operation r) {
        return YearlyHistoryView.builder()
                .id(ObjectIdHelper.id())
                .curAmount(r.getAmount())
                .refAmount(BigDecimal.ZERO)
                .year(r.getOperationDate().getYear())
                .date(r.getOperationDate())
                .account(r.getAccount());
    }
}
