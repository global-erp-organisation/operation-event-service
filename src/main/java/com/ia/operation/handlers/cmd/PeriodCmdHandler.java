package com.ia.operation.handlers.cmd;

import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.commands.creation.PeriodCreationCmd;
import com.ia.operation.handlers.Handler;
import com.ia.operation.util.PeriodGenerator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PeriodCmdHandler implements Handler {
    private final CommandGateway gateway;
    private final PeriodGenerator generator;

    public Mono<ServerResponse> periodAdd(ServerRequest request) {
        final Mono<PeriodCreationCmd> bodyMono = request.bodyToMono(PeriodCreationCmd.class).filter(body -> body.getYear() != null);
        return bodyMono.map(body -> {
            return generator.generate(body.getYear()).stream().map(e -> {
                return gateway.sendAndWait(PeriodCreationCmd.cmdFrom(e).build());
            }).map(o -> o.toString()).collect(Collectors.toList());
        }).flatMap(ids -> ServerResponse.accepted().body(Flux.fromIterable(ids), String.class))
                .switchIfEmpty(ServerResponse.badRequest().body(Mono.just(MISSING_REQUEST_BODY_KEY + " or the year property is missing"), String.class));

    }
}
