package com.ia.operation.configuration;

import lombok.RequiredArgsConstructor;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.eventsourcing.AggregateSnapshotter;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.serialization.Serializer;
import org.axonframework.springboot.util.RegisterDefaultEntities;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty("axon.events.database")
@RegisterDefaultEntities(packages = {"org.axonframework.eventsourcing.eventstore.jpa"})
public class EventJpaStorageConfiguration {

    private final AxonProperties properties;

  @Bean
    public EventStorageEngine eventStoreEngine(EntityManagerProvider entityManagerProvider, TransactionManager transactionManager) {
        return JpaEventStorageEngine.builder().entityManagerProvider(entityManagerProvider).transactionManager(transactionManager).build();
    }

    @Bean
    public EntityManagerProvider entityManagerProvider(EntityManager entityManager) {
        return new SimpleEntityManagerProvider(entityManager);
    }

    @Bean
    EventStore eventStore(EventStorageEngine eventStorageEngine) {
        return EmbeddedEventStore.builder()
                .storageEngine(eventStorageEngine)
                .build();
    }

    @Bean
    public TokenStore tokenStore(Serializer serializer, EntityManagerProvider entityManagerProvider) {
        return  JpaTokenStore.builder().serializer(serializer).entityManagerProvider(entityManagerProvider).build();
   }


     @Bean
    public Snapshotter snapshotter(ParameterResolverFactory parameterResolverFactory, EventStore eventStore, TransactionManager transactionManager) {
        final Executor executor = Executors.newSingleThreadExecutor();
        return AggregateSnapshotter.builder()
                .parameterResolverFactory(parameterResolverFactory)
                .eventStore(eventStore)
                .executor(executor)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(Snapshotter snapshotter) throws Exception {
        return new EventCountSnapshotTriggerDefinition(snapshotter, properties.getSnapshotLimit());
    }
}
