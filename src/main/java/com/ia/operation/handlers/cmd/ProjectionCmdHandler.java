package com.ia.operation.handlers.cmd;

import java.util.List;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ia.operation.commands.creation.ProjectionCreationCmd;
import com.ia.operation.commands.delete.ProjectionDeleteCmd;
import com.ia.operation.commands.update.ProjectionUpdateCmd;
import com.ia.operation.events.created.ProjectionCreatedEvent;
import com.ia.operation.events.created.ProjectionGeneratedEvent;
import com.ia.operation.util.ObjectIdUtil;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping
@AllArgsConstructor
public class ProjectionCmdHandler {
    
    private final CommandGateway gateway;
    private final RabbitTemplate rabbitTemplate;
    
    @PostMapping(value = "/operations/{operationId}/periods/{periodId}/projections")
    public ResponseEntity<String> projectionAdd(@RequestBody ProjectionCreationCmd request, @PathVariable String operationId,
                                                @PathVariable String periodId) {
        final ProjectionCreationCmd cmd = ProjectionCreationCmd
                .from(request)
                .id(ObjectIdUtil.id())
                .operationId(operationId)
                .periodId(periodId)
                .build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }
    
    @PutMapping(value = "/projections/{projectionId}")
    public ResponseEntity<String> projectionUpdate(@RequestBody ProjectionUpdateCmd request, @PathVariable String projectionId) {
        final ProjectionUpdateCmd cmd = ProjectionUpdateCmd
                .from(request)
                .id(projectionId)
                .build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }

    
    @DeleteMapping(value = "/projections/{projectionId}")
    public ResponseEntity<String> projectionDelete(@PathVariable String projectionId) {
        final ProjectionDeleteCmd cmd = ProjectionDeleteCmd.builder().id(projectionId).build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }


    @PostMapping(value = "/periods/{year}/projections")
    public ResponseEntity<Object> projectionGenerate(@PathVariable int year) {
        rabbitTemplate.convertAndSend("cmd", ProjectionGeneratedEvent.builder().year(year).build());
        return ResponseEntity.accepted().body("Projection generation command succesfully recieved.");
    }
    
    @RabbitListener(queues= {"projection-event-queue"})
    public void handleProjectionGenerationEvents(List<ProjectionCreatedEvent> events) {
        events.forEach(event -> gateway.send(ProjectionCreationCmd.from(event).build()));
    }
}
