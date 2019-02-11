package com.ia.operation.handlers.cmd;

import java.time.LocalDate;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ia.operation.commands.creation.RealisationCreationCmd;
import com.ia.operation.commands.delete.RealisationDeleteCmd;
import com.ia.operation.commands.update.RealisationUpdateCmd;
import com.ia.operation.util.ObjectIdUtil;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class RealisationCmdHandler {
    private final CommandGateway gateway;
    @PostMapping(value = "operations/{operationId}/realisations")
    public ResponseEntity<String> realisationAdd(@RequestBody RealisationCreationCmd request, @PathVariable String operationId) {
        final LocalDate date = request.getOperationDate() == null ? LocalDate.now() : request.getOperationDate();
        final RealisationCreationCmd cmd = RealisationCreationCmd
                .from(request)
                .id(ObjectIdUtil.id())
                .operationDate(date)
                .operationId(operationId)
                .build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }

    @PutMapping(value = "/realisations/{realisationId}")
    public ResponseEntity<String> realisationAdd(@RequestBody RealisationUpdateCmd request, @PathVariable String realisationId) {
        final RealisationUpdateCmd cmd = RealisationUpdateCmd
                .from(request)
                .id(realisationId)
                .build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }

    @DeleteMapping(value = "/realisations/{realisationId}")
    public ResponseEntity<String> realisationAdd(@PathVariable String realisationId) {
        final RealisationDeleteCmd cmd = RealisationDeleteCmd.builder().id(realisationId).build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }
}
