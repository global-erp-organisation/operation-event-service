package com.ia.operation.documents.views;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HistoryView<T> {
    private LocalDate start;
    private LocalDate end;
    private List<T> details;
}
