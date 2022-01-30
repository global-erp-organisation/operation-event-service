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

    public  <V extends ICommand> Mono<ServerResponse> doExecute(Mono<ValidationResult<V>> cmd) {
        try {
            beanValidate(cmd);
            return cmd.flatMap(this::response).switchIfEmpty(badRequestError(MISSING_REQUEST_BODY_KEY));
        } catch (Exception e) {
            return internalErrorResponse(e::getLocalizedMessage);
        }
    }

    public  <V extends ICommand> Mono<ServerResponse> response(ValidationResult<V> cmd) {
        if (cmd.isValid()) {
            return cmd.getValidated().map(command -> {
                final String id = gateway.sendAndWait(command);
                final CmdResponse<ICommand, String> cr = CmdResponse.<ICommand, String>builder().body(command).build();
                return doAcceptedRequest(() -> Mono.just(cr), CmdResponse.class);
            }).orElse(badRequestError("No error message found. It sound like a default validator operation have been used."));
        } else {
            return badRequestError(cmd.getErrors());
        }
    }

    public  <T> Mono<ServerResponse> doAcceptedRequest(Supplier<Publisher<T>> response, Class<T> responseType) {
        try {
            beanValidate(response, responseType);
            return ServerResponse.accepted().body(response.get(), responseType);
        } catch (Exception e) {
            return internalErrorResponse(e::getLocalizedMessage);
        }
    }
}
