package com.ia.operation.documents.views;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(value=Include.NON_EMPTY)
public class HistoryView<T> {
    private List<T> details;
}
