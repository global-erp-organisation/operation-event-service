package com.ia.operation.handlers;

import java.util.Optional;
import java.util.function.Supplier;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.Assert;
import org.axonframework.queryhandling.QueryGateway;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.handlers.CmdResponse.Error;
import com.ia.operation.util.validator.CommandValidator;

import reactor.core.publisher.Mono;

public interface Handler {
    String ACCOUNT_ID_KEY = "accountId";
    String CATEGORY_ID_KEY = "categoryId";
    String COMPANY_ID_KEY = "companyId";
    String MISSING_REQUEST_BODY_KEY = "The request body is missing.";
    String OPERATION_ID_KEY = "operationId";
    String PERIOD_ID_KEY = "periodId";
    String PROJECTION_ID_KEY = "projectionId";
    String USER_ID_KEY = "userId";
    String YEAR_KEY = "year";

    default <V> Mono<ServerResponse> commandComplete(Mono<CommandValidator.ValidationResult<V>> cmd, CommandGateway gateway) {
        try {
            beanValidate(cmd, gateway);
            return cmd.flatMap(r -> response(r, gateway))
                    .switchIfEmpty(ServerResponse.badRequest().body(Mono.just(MISSING_REQUEST_BODY_KEY), String.class));
        } catch (Exception e) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(e.getMessage()), String.class);
        }
    }

    default <V> Mono<ServerResponse> response(CommandValidator.ValidationResult<V> cmd, CommandGateway gateway) {
        if (cmd.getIsValid()) {
            final Optional<CommandValidator<V>> r = cmd.getValidated();
            if (r.isPresent()) {
                final CommandValidator<V> command = r.get();
                final String id = gateway.sendAndWait(command);
                final CmdResponse<String, String> cr = CmdResponse.<String, String>builder().body(id == null ? command.getId() : id).build();
                return acceptedRequestComplete(() -> Mono.just(cr), CmdResponse.class);
            } else {
                return badRequestError("No error message found. It sound like a default validator operation have been used.");
            }
        } else {
            return badRequestError(cmd.getErrors());
        }
    }

    default <T> Mono<ServerResponse> acceptedRequestComplete(Supplier<Publisher<T>> response, Class<T> responseType) {
        try {
            beanValidate(response, responseType);
            return ServerResponse.accepted().body(response.get(), responseType);
        } catch (Exception e) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(e.getMessage()), String.class);
        }
    }

    default <T> Mono<ServerResponse> badRequestComplete(Supplier<T> message, Class<T> type) {
        beanValidate(message);
        return ServerResponse.badRequest().body(Mono.just(message.get()), type);
    }

    default <T, E> Mono<ServerResponse> errorWithSatus(E errorDescription, HttpStatus status) {
        final Error<E> error = Error.<E>builder().status(status).body(errorDescription).build();
        return badRequestComplete(() -> CmdResponse.<T, E>builder().error(error).build(), CmdResponse.class);
    }

    default <E> Mono<ServerResponse> badRequestError(E errorDescription) {
        return errorWithSatus(errorDescription, HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings("unchecked")
    default <Q, T> Mono<ServerResponse> queryComplete(Supplier<Q> supplier, Class<T> responseType, QueryGateway gateway) {
        try {
            beanValidate(supplier, responseType, gateway);
            return ServerResponse.ok().body((Publisher<T>) gateway.query(supplier.get(), Object.class).get(), responseType);
        } catch (Exception e) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Mono.just(e.getLocalizedMessage()), String.class);
        }
    }

    @ExceptionHandler
    default void beanValidate(Object... beans) {
        for (Object o : beans) {
            Assert.notNull(o, () -> "null is not accepted for " + o.getClass().getSimpleName() + " object");
        }
    }
}
