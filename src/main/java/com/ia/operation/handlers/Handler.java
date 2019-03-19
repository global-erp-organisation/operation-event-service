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

import com.ia.operation.commands.ICommand;
import com.ia.operation.handlers.CmdResponse.Error;
import com.ia.operation.util.validator.CommandValidator;
import com.ia.operation.util.validator.CommandValidator.ValidationResult;

import reactor.core.publisher.Mono;

public interface Handler extends ConstantHandler {

    default <V extends ICommand> Mono<ServerResponse> commandComplete(Mono<ValidationResult<V>> cmd, CommandGateway gateway) {
        try {
            beanValidate(cmd, gateway);
            return cmd.flatMap(r -> response(r, gateway)).switchIfEmpty(badRequestError(MISSING_REQUEST_BODY_KEY));
        } catch (Exception e) {
            return internalErrorResponse(() -> e.getLocalizedMessage());
        }
    }

    default <V extends ICommand> Mono<ServerResponse> response(ValidationResult<V> cmd, CommandGateway gateway) {
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
            return internalErrorResponse(() -> e.getLocalizedMessage());
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
            return internalErrorResponse(() -> e.getLocalizedMessage());
        }
    }

    default <E> Mono<ServerResponse> internalErrorResponse(Supplier<E> message) {
        return errorWithSatus(message.get(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    default void beanValidate(Object... beans) {
        for (Object o : beans) {
            Assert.notNull(o, () -> "null is not accepted for " + o.getClass().getSimpleName() + " object");
        }
    }
}
