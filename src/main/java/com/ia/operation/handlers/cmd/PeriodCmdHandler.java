package com.ia.operation.handlers.cmd;

import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ia.operation.commands.creation.PeriodCreationCmd;
import com.ia.operation.util.PeriodGenerator;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/periods")
public class PeriodCmdHandler {
    private final CommandGateway gateway;
    private final PeriodGenerator generator;

    @PostMapping
    public ResponseEntity<List<String>> periodAdd(@RequestBody PeriodCreationCmd request) {
        List<String> ids = generator.generate(request.getYear()).stream().map(e -> {
            return gateway.sendAndWait(PeriodCreationCmd.from(e).build());
        }).map(o -> o.toString()).collect(Collectors.toList());
        return ResponseEntity.accepted().body(ids);
    }
}
