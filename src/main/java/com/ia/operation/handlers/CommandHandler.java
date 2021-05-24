package com.ia.operation.handlers;

import com.ia.operation.commands.ICommand;
import com.ia.operation.helper.validator.CommandValidator;
import com.ia.operation.helper.validator.CommandValidator.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CommandHandler implements Handler {

    private final CommandGateway gateway;

    public  <V extends ICommand> Mono<ServerResponse> commandComplete(Mono<ValidationResult<V>> cmd) {
        try {
            beanValidate(cmd);
            return cmd.flatMap(this::response).switchIfEmpty(badRequestError(MISSING_REQUEST_BODY_KEY));
        } catch (Exception e) {
            return internalErrorResponse(e::getLocalizedMessage);
        }
    }

    public  <V extends ICommand> Mono<ServerResponse> response(ValidationResult<V> cmd) {
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

    public  <T> Mono<ServerResponse> acceptedRequestComplete(Supplier<Publisher<T>> response, Class<T> responseType) {
        try {
            beanValidate(response, responseType);
            return ServerResponse.accepted().body(response.get(), responseType);
        } catch (Exception e) {
            return internalErrorResponse(e::getLocalizedMessage);
        }
    }

    public  <T> Mono<ServerResponse> badRequestComplete(Supplier<T> message, Class<T> type) {
        beanValidate(message);
        return ServerResponse.badRequest().body(Mono.just(message.get()), type);
    }
}
