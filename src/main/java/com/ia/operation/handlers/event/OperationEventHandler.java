package com.ia.operation.handlers.event;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.ia.operation.documents.Operation;
import com.ia.operation.events.created.OperationCreatedEvent;
import com.ia.operation.events.deleted.OperationDeletedEvent;
import com.ia.operation.events.updated.OperationUpdatedEvent;
import com.ia.operation.queries.operation.OperationGetByUser;
import com.ia.operation.queries.operation.OperationGetByIdQuery;
import com.ia.operation.repositories.OperationCategoryRepository;
import com.ia.operation.repositories.OperationRepository;
import com.ia.operation.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
@ProcessingGroup("operation-handler")
public class OperationEventHandler {
    private final OperationRepository operationRepository;
    private final UserRepository userRepository;
    private final OperationCategoryRepository operationCategoryRepository;

    @EventHandler
    public void on(OperationCreatedEvent event) {
        log.info("event recieved: value=[{}]", event);
        userRepository.findById(event.getUserId()).subscribe(user -> {
            operationCategoryRepository.findById(event.getCategoryId()).subscribe(category -> {
                operationRepository.save(Operation.of(event, user, category)).subscribe(o -> {
                    log.info("Operation successfully saved. value=[{}]", o);
                });
            });
        });
    }

    @EventHandler
    public void on(OperationUpdatedEvent event) {
        log.info("event recieved: value=[{}]", event);
        userRepository.findById(event.getUserId()).subscribe(user -> {
            operationCategoryRepository.findById(event.getCategoryId()).subscribe(category -> {
                operationRepository.save(Operation.of(event, user, category)).subscribe(o -> {
                    log.info("Operation successfully saved. value=[{}]", o);
                });
            });
        });
    }

    @EventHandler
    public void on(OperationDeletedEvent event) {
        log.info("event recieved: value=[{}]", event);
        operationRepository.deleteById(event.getId()).subscribe(e -> {
            log.info("Operation successfully removed. value=[{}]", event.getId());
        });
    }

    @QueryHandler
    public Object operationGet(OperationGetByIdQuery query) {
        return operationRepository.findById(query.getOperationId());
    }

    @QueryHandler
    public Object operationGetByUser(OperationGetByUser query) {
        return operationRepository.findByUser_id(query.getUserId());
    }

}
