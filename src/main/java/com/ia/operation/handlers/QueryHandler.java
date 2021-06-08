package com.ia.operation.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueryHandler implements Handler {

    private final QueryGateway gateway;

    public  <Q, T> Mono<ServerResponse> query(Supplier<Q> querySupplier, Class<T> responseType) {
        try {
            beanValidate(querySupplier, responseType);
            final CompletableFuture<T> future = gateway.query(querySupplier.get(), ResponseTypes.instanceOf(responseType));
            final Mono<T> response = Mono.fromFuture(future);
            return ServerResponse.ok().body(response, responseType);
        } catch (Exception e) {
            return internalErrorResponse(e::getLocalizedMessage);
        }
    }

    public  <Q, T> Mono<ServerResponse> queryList(Supplier<Q> querySupplier, Class<T> responseType) {
        try {
            beanValidate(querySupplier, responseType);
            final Mono<List<T>> response = Mono.fromFuture(gateway.query(querySupplier.get(), ResponseTypes.multipleInstancesOf(responseType)));
            return ServerResponse.ok().body(response, new ParameterizedTypeReference<List<T>>() {
                @Override
                public Type getType() { return super.getType(); }
            });
        } catch (Exception e) {
            return internalErrorResponse(e::getLocalizedMessage);
        }
    }

    public  <Q, T> Mono<ServerResponse> subscribe(Supplier<Q> querySupplier, Class<T> responseType) {
        try {
            beanValidate(querySupplier, responseType);
            final Flux<T> response = gateway.subscriptionQuery(querySupplier.get(), ResponseTypes.instanceOf(responseType), ResponseTypes.instanceOf(responseType)).updates();
            return ServerResponse.ok().body(response, responseType);
        } catch (Exception e) {
            return internalErrorResponse(e::getLocalizedMessage);
        }
    }
}
