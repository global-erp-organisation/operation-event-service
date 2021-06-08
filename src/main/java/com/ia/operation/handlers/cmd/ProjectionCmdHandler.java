package com.ia.operation.handlers.cmd;

import com.ia.operation.commands.creation.ProjectionCreationCmd;
import com.ia.operation.commands.delete.ProjectionDeletionCmd;
import com.ia.operation.commands.update.ProjectionUpdateCmd;
import com.ia.operation.configuration.axon.AxonProperties;
import com.ia.operation.events.created.ProjectionCreatedEvent;
import com.ia.operation.events.created.ProjectionGeneratedEvent;
import com.ia.operation.handlers.CmdResponse;
import com.ia.operation.handlers.CommandHandler;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.ObjectIdHelper;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProjectionCmdHandler extends CommandHandler {
    private final CommandGateway gateway;
    private final RabbitTemplate rabbitTemplate;
    private final AggregateHelper util;
    private final AxonProperties properties;

    public ProjectionCmdHandler(CommandGateway gateway, RabbitTemplate rabbitTemplate, AggregateHelper util, AxonProperties properties) {
        super(gateway);
        this.gateway = gateway;
        this.rabbitTemplate = rabbitTemplate;
        this.util = util;
        this.properties = properties;
    }

    public Mono<ServerResponse> projectionAdd(ServerRequest request) {
        final String accountId = request.pathVariable(ACCOUNT_ID_KEY);
        final String periodId = request.pathVariable(PERIOD_ID_KEY);
        final Mono<ProjectionCreationCmd> bodyMono = request.bodyToMono(ProjectionCreationCmd.class);
        return doExecute(
                bodyMono.map(body -> ProjectionCreationCmd.cmdFrom(body).id(ObjectIdHelper.id()).accountId(accountId).periodId(periodId).build().validate(util)));
    }

    public Mono<ServerResponse> projectionUpdate(ServerRequest request) {
        final Mono<ProjectionUpdateCmd> bodyMono = request.bodyToMono(ProjectionUpdateCmd.class);
        final String projectionId = request.pathVariable(PROJECTION_ID_KEY);
        return doExecute(bodyMono.map(body -> ProjectionUpdateCmd.cmdFrom(body).id(projectionId).build().validate(util)));

    }

    public Mono<ServerResponse> projectionDelete(ServerRequest request) {
        final String projectionId = request.pathVariable(PROJECTION_ID_KEY);
        return response(ProjectionDeletionCmd.builder().id(projectionId).build().validate(util));
    }

    public Mono<ServerResponse> projectionGenerate(ServerRequest request) {
        final String year = request.pathVariable(YEAR_KEY);
        if (StringUtil.isNullOrEmpty(year) || !StringUtils.isNumeric(year)) {
            return badRequestComplete(()->"The year parametter is missing or the provided variable is not a number.", String.class);
        }
        rabbitTemplate.convertAndSend(properties.getDefaultCmdRoutingKey(), ProjectionGeneratedEvent.builder().year(year).build());
        return doAcceptedRequest(() -> Mono.just(CmdResponse.<String, String>builder().body("Projection generation command succesfully recieved.").build()), CmdResponse.class);
    }

    @RabbitListener(queues = {"${axon.events.projection-event-queue}"})
    public void handleProjectionGeneratedEvents(ProjectionCreatedEvent event) {
        gateway.send(ProjectionCreationCmd.cmdFrom(event).build());
    }
}
