package com.ia.operation.util;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.GenericMessage;
import org.axonframework.messaging.unitofwork.DefaultUnitOfWork;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.modelling.command.Repository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@ConditionalOnProperty("axon.events.database")
@Component
@AllArgsConstructor
public class AggregateUtil {
    private final EventStore store;

    public <T> Optional<T> aggregateGet(String aggregateId, Class<T> clazz) {
        final UnitOfWork<GenericMessage<String>> uow = DefaultUnitOfWork.startAndGet(new GenericMessage<>(aggregateId));
        try {

            final Aggregate<T> aggreagte = getRepository(clazz).load(aggregateId);
            return Optional.of(aggreagte.invoke(a -> {
                uow.commit();
                return a;
            }));
        } catch (AggregateNotFoundException e) {
            uow.rollback();
            return Optional.empty();
        }
    }

    public <T> Collection<T> aggregateGet(Collection<String> aggregateIds, Class<T> clazz) {
        return aggregateIds.stream().map(a -> aggregateGet(a, clazz)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    public <T> Repository<T> getRepository(Class<T> clazz) {
        return EventSourcingRepository.builder(clazz).eventStore(store).build();
    }
    
    
}
