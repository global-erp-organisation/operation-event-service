package com.ia.operation.handlers.cmd;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ia.operation.commands.creation.OperationCreationCmd;
import com.ia.operation.commands.delete.OperationDeleteCmd;
import com.ia.operation.commands.update.OperationUpdateCmd;
import com.ia.operation.util.ObjectIdUtil;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class OperationCmdHandler {
    private final CommandGateway gateway;

    @PostMapping(value = "users/{userId}/operations")
    public ResponseEntity<String> operationAdd(@RequestBody OperationCreationCmd request, @PathVariable String userId) {
        final OperationCreationCmd cmd = OperationCreationCmd.from(request).id(ObjectIdUtil.id()).userId(userId).build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }

    @PutMapping(value = "/operations/{operationId}")
    public ResponseEntity<String> operationUpdate(@RequestBody OperationUpdateCmd request, @PathVariable String operationId) {
        final OperationUpdateCmd cmd = OperationUpdateCmd.from(request).id(operationId).build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }

    @DeleteMapping(value = "/operations/{operationId}")
    public ResponseEntity<String> operationDelete(@PathVariable String operationId) {
        final OperationDeleteCmd cmd = OperationDeleteCmd.builder().id(operationId).build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }

}
