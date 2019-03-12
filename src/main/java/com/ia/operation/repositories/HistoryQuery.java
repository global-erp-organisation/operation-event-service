package com.ia.operation.repositories;

import java.time.LocalDate;

import reactor.core.publisher.Flux;

public interface HistoryQuery<T> {
 Flux<T> findByAccount_User_IdAndDateBetweenOrderByDate(String userId, LocalDate start, LocalDate end);
}
