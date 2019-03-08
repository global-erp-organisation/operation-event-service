package com.ia.operation.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ia.operation.documents.views.HistoryView;

public interface HistoryRepository<T> extends ReactiveMongoRepository<HistoryView<T>, String> {

}
