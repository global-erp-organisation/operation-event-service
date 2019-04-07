package com.ia.operation.helper.history;

import com.ia.operation.documents.Operation;

public interface HistoryUpdater<T> {
    void update(Operation current, Operation old, UpdateType type);
}
