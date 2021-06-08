package com.ia.operation.helper.history;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

public interface HistoryUpdater<T, O> {
    /**
     * Update the history lines according th e incoming operation.
     * @param current current incoming operation.
     * @param old related old operation.
     * @param type Update mode (Creation, Update or Deletion mode).
     */
    void update(O current, O old, UpdateType type);
    
    /**
     * Check if two operations are equal according to the current history updater type.
     * @param first First operation that need to be compared.
     * @param second Second operation that need to be compared.
     * @return true if provided operations are equal according to the current history updater type.
     */
    boolean isEqual(O first, O second);

    /**
     *
     * @param current current date
     * @return The previous and the next date encapsulated in the PeriodParams object.
     */
    PeriodParams getPeriodParams(LocalDate current);

    void onAdd(O event);
    void onUpdate(O event, O old);
    void onRemove(O event);

    default void proceed(O event, O old, UpdateType type) {
        switch (type) {
            case A:
                onAdd(event);
                break;
            case U:
                onUpdate(event, old);
                break;
            case R:
                onRemove(event);
                break;
            default:
                throw new IllegalArgumentException("Unknown update type.");
        }
    }

    @Value
    @Builder
    class PeriodParams {
         LocalDate previous;
         LocalDate next;
    }
}
