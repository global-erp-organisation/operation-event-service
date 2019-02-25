package com.ia.operation.handlers;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.util.validator.CommandValidator;

import reactor.core.publisher.Mono;

public interface Handler {
    
    String COMPANY_ID = "companyId";
    String MISSING_REQUEST_BODY = "The request body is missing.";
    String CATEGORY_ID = "categoryId";
    String USER_ID = "userId";
    String OPERATION_ID = "operationId";
    String PERIOD_ID = "periodId";
    String ACCOUNT_ID = "accountId";
    String PROJECTION_ID = "projectionId";
    String YEAR= "year";
    
    default <V> Mono<ServerResponse> response(CommandValidator.ValidationResult<V> result, CommandGateway gateway) {
        if (result.getIsValid()) {
            final Optional<CommandValidator<V>> r = result.getValidated();
            if (r.isPresent()) {
                final CommandValidator<V> command = r.get();
                final String id = gateway.sendAndWait(command);
                return ServerResponse.accepted()
                        .body(id == null ? Mono.just(command.getClass().getSimpleName() + " successfully applied.") : Mono.just(id), String.class);
            } else {
                return ServerResponse.badRequest().body(Mono.just("No error message found. It sound like a default validator operation have been used."),
                        String.class);
            }
        } else {
            return ServerResponse.badRequest().body(Mono.just(result.getErrors()), List.class);
        }
    }

    default <V> Mono<ServerResponse> commandComplete(Mono<CommandValidator.ValidationResult<V>> cmd, CommandGateway gateway) {
        return cmd.flatMap(r -> response(r, gateway)).switchIfEmpty(ServerResponse.badRequest().body(Mono.just(MISSING_REQUEST_BODY), String.class));
    }

    default Mono<ServerResponse> queryVariableBadRequest(String variable) {
        return ServerResponse.badRequest().body(Mono.just("The " + variable + " variable is missing."), String.class);
    }

    @SuppressWarnings("unchecked")
    default <Q, T> Mono<ServerResponse> queryComplete(Supplier<Q> supplier, Class<T> responseType, QueryGateway gateway) {
        try {
            return ServerResponse.ok().body((Publisher<T>) gateway.query(supplier.get(), Object.class).get(), responseType);
        } catch (Exception e) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(e.getMessage()), String.class);
        }
    }
}
