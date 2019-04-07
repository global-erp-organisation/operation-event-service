package com.ia.operation.configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty("axon.events.database")
public class EventStorageConfiguration {

    private final AxonProperties properties;

    @Bean
    public EventStorageEngine engineStorage(MongoTemplate template) {
        final MongoEventStorageEngine engine =  MongoEventStorageEngine.builder()
                .mongoTemplate(template)
                .build();
        engine.ensureIndexes();
        return engine;
    }

    @Bean
    EventStore eventStore(EventStorageEngine eventStorageEngine) {
        return EmbeddedEventStore.builder()
                .storageEngine(eventStorageEngine)
                .build();
    }

    @Bean
    public MongoTemplate template(MongoClient client) {
        return DefaultMongoTemplate.builder()
                .mongoDatabase(client, properties.getDatabase())
                .build();
    }

     @Bean
    public TokenStore tokenStore(Serializer serializer, MongoTemplate template) {
        return MongoTokenStore.builder()
                .serializer(serializer)
                .mongoTemplate(template)
                .build();
    }

     @Bean
    public Snapshotter snapshotter(ParameterResolverFactory parameterResolverFactory, EventStore eventStore, TransactionManager transactionManager) {
        final Executor executor = Executors.newSingleThreadExecutor();
        return SpringAggregateSnapshotter.builder()
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
