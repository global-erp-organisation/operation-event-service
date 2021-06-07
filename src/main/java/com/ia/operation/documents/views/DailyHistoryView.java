package com.ia.operation.documents.views;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.documents.Account;
import com.ia.operation.documents.Operation;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = Include.NON_EMPTY)
public class DailyHistoryView {
    @Id
    private String id;
    @JsonProperty("ref_amount")
    private BigDecimal refAmount;
    @JsonProperty("cur_amount")
    private BigDecimal curAmount;
    //@ManyToOne(fetch = FetchType.EAGER)
    private Account account;
    private LocalDate date;

    public static DailyHistoryViewBuilder from(Operation r) {
        /*@formatter:off*/
        return DailyHistoryView.builder()
                .date(r.getOperationDate())
                .refAmount(BigDecimal.ZERO)
                .account(r.getAccount())
                .curAmount(r.getAmount());
        /*@formatter:on*/
    }

    public static DailyHistoryViewBuilder from(DailyHistoryView r) {
        /*@formatter:off*/
        return DailyHistoryView.builder()
                .id(r.getId())
                .date(r.getDate())
                .account(r.getAccount())
                .refAmount(r.getRefAmount())
                .curAmount(r.getCurAmount());
        /*@formatter:on*/
    }
}
