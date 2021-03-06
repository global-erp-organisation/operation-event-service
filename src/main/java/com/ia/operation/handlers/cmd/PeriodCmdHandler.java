package com.ia.operation.handlers.cmd;

import com.ia.operation.commands.creation.PeriodCreationCmd;
import com.ia.operation.handlers.CmdResponse;
import com.ia.operation.handlers.CommandHandler;
import com.ia.operation.helper.PeriodGenerator;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PeriodCmdHandler extends CommandHandler {
    private final CommandGateway gateway;
    private final PeriodGenerator generator;

    public PeriodCmdHandler(CommandGateway gateway, PeriodGenerator generator) {
        super(gateway);
        this.gateway = gateway;
        this.generator = generator;
    }

    public Mono<ServerResponse> periodAdd(ServerRequest request) {
        final Mono<PeriodCreationCmd> bodyMono = request.bodyToMono(PeriodCreationCmd.class).filter(body -> body.getYear() != null);
        return bodyMono.map(body -> {
            return generator.generate(body.getYear()).stream().map(e -> gateway.sendAndWait(PeriodCreationCmd.cmdFrom(e).build())).map(Object::toString).collect(Collectors.toList());
        }).flatMap(ids -> {
            final CmdResponse<List<String>, List<String>> res = CmdResponse.<List<String>, List<String>>builder().body(ids).build();
            return ServerResponse.accepted().body(Mono.just(res), CmdResponse.class);
        }).switchIfEmpty(ServerResponse.badRequest().body(Mono.just(MISSING_REQUEST_BODY_KEY + " or the year property is missing"), String.class));
    }
}
