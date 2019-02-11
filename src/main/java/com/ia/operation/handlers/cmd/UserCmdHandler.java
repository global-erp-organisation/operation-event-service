package com.ia.operation.handlers.cmd;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ia.operation.commands.creation.UserCreationCmd;
import com.ia.operation.commands.delete.UserDeleteCmd;
import com.ia.operation.commands.update.UserUpdateCmd;
import com.ia.operation.util.ObjectIdUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class UserCmdHandler {
    private CommandGateway gateway;

    @PostMapping(value = "companies/{companyId}/users")
    public ResponseEntity<String> userAdd(@RequestBody UserCreationCmd request, @PathVariable String companyId) {
        final UserCreationCmd cmd = UserCreationCmd.from(request).id(ObjectIdUtil.id()).companyId(companyId).build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }

    @PutMapping(value = "/users/{userId}")
    public ResponseEntity<String> userUpdate(@RequestBody UserUpdateCmd request, @PathVariable String userId) {
        final UserUpdateCmd cmd = UserUpdateCmd.from(request).id(userId).build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }

    @DeleteMapping(value = "/users/{userId}")
    public ResponseEntity<String> userDelete(@PathVariable String userId) {
        final UserDeleteCmd cmd = UserDeleteCmd.builder().id(userId).build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }
}
