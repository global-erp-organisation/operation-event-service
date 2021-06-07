package com.ia.operation.helper.history;

import com.ia.operation.documents.Operation;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

public interface HistoryUpdater<T> {
    /**
     * Update the history lines accourding th e incoming operation.
     * @param current current incoming operation.
     * @param old related old operation.
     * @param type Update mode (Creation, Update or Deletion mode).
     */
    void update(Operation current, Operation old, UpdateType type);
    
    /**
     * Check if two operations are equal according to the current hystory updater type.
     * @param first First operation that need to be compared.
     * @param second Second operation that need to be compared.
     * @return true if provided operations are equal according to the current hystory updater type.
     */
    boolean isEqual(Operation first, Operation second);

    /**
     *
     * @param current current date
     * @return The previous and the next date encapsulated in thee PeriodParams object.
     */
    PeriodParams getPeriodParams(LocalDate current);



    @Value
    @Builder
    class PeriodParams {
         LocalDate previous;
         LocalDate next;
    }
}
