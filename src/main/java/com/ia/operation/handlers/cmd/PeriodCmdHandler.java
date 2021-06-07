package com.ia.operation.handlers.cmd;

import com.ia.operation.commands.creation.PeriodCreationCmd;
import com.ia.operation.handlers.CmdResponse;
import com.ia.operation.handlers.CommandHandler;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.PeriodGenerator;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PeriodCmdHandler extends CommandHandler {
    private final PeriodGenerator generator;
    private final AggregateHelper util;
    public PeriodCmdHandler(CommandGateway gateway, PeriodGenerator generator, AggregateHelper util) {
        super(gateway);
        this.generator = generator;
        this.util = util;
    }

    public Mono<ServerResponse> periodAdd(ServerRequest request) {
        final Optional<String> year = request.queryParam(YEAR_KEY);
        return year.map(y -> {
            final List<PeriodCreationCmd> cmds = generator.generate(y).stream()
                    .map(c -> PeriodCreationCmd.cmdFrom(c).year(y).build()).collect(Collectors.toList());
            cmds.forEach(c -> response(c.validate(util)));
            return doAcceptedRequest(() -> Mono.just(CmdResponse.<List<PeriodCreationCmd>, String>builder().body(cmds).build()), CmdResponse.class);
        }).orElse(badRequestComplete(() -> CmdResponse.<String, String>builder().body(String.format("%s%s",MISSING_QUERY_PARAM_PREFIX, YEAR_KEY)).build(), CmdResponse.class));
    }
}
