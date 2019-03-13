package com.ia.operation.handlers.cmd;

import org.apache.commons.lang3.StringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.commands.creation.ProjectionCreationCmd;
import com.ia.operation.commands.delete.ProjectionDeletionCmd;
import com.ia.operation.commands.update.ProjectionUpdateCmd;
import com.ia.operation.events.created.ProjectionCreatedEvent;
import com.ia.operation.events.created.ProjectionGeneratedEvent;
import com.ia.operation.handlers.Handler;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.ObjectIdUtil;

import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class ProjectionCmdHandler implements Handler {
    private final CommandGateway gateway;
    private final RabbitTemplate rabbitTemplate;
    private final AggregateUtil util;

    public Mono<ServerResponse> projectionAdd(ServerRequest request) {
        final String accountId = request.pathVariable(ACCOUNT_ID_KEY);
        final String periodId = request.pathVariable(PERIOD_ID_KEY);
        final Mono<ProjectionCreationCmd> bodyMono = request.bodyToMono(ProjectionCreationCmd.class);
        return commandComplete(
                bodyMono.map(
                        body -> ProjectionCreationCmd.cmdFrom(body).id(ObjectIdUtil.id()).accountId(accountId).periodId(periodId).build().validate(util)),
                gateway);
    }

    public Mono<ServerResponse> projectionUpdate(ServerRequest request) {
        final Mono<ProjectionUpdateCmd> bodyMono = request.bodyToMono(ProjectionUpdateCmd.class);
        final String projectionId = request.pathVariable(PROJECTION_ID_KEY);
        return commandComplete(bodyMono.map(body -> ProjectionUpdateCmd.cmdFrom(body).id(projectionId).build().validate(util)), gateway);

    }

    public Mono<ServerResponse> projectionDelete(ServerRequest request) {
        final String projectionId = request.pathVariable(PROJECTION_ID_KEY);
        return response(ProjectionDeletionCmd.builder().id(projectionId).build().validate(util), gateway);
    }

    public Mono<ServerResponse> projectionGenerate(ServerRequest request) {
        final String year = request.pathVariable(YEAR_KEY);
        if (StringUtil.isNullOrEmpty(year) || !StringUtils.isNumeric(year)) {
            return ServerResponse.badRequest().body(Mono.just("The year parametter is missing or the provided variable is not a number."), String.class);
        }
        rabbitTemplate.convertAndSend("cmd", ProjectionGeneratedEvent.builder().year(year).build());
        return ServerResponse.accepted().body(Mono.just("Projection generation command succesfully recieved."), String.class);
    }

    @RabbitListener(queues = {"projection-event-queue"})
    public void handleProjectionGenerationEvents(ProjectionCreatedEvent event) {
        gateway.send(ProjectionCreationCmd.cmdFrom(event).build());
    }
}
