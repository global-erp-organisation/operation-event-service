package com.ia.operation.handlers.cmd;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ia.operation.commands.creation.CompanyCreationCmd;
import com.ia.operation.commands.delete.CompanyDeleteCmd;
import com.ia.operation.commands.update.CompanyUpdateCmd;
import com.ia.operation.util.ObjectIdUtil;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/companies")
public class CompanyCmdHandler {
    private final CommandGateway gateway;

    @PostMapping
    public ResponseEntity<String> companyAdd(@RequestBody CompanyCreationCmd request) {
        final CompanyCreationCmd cmd = CompanyCreationCmd.of(request).id(ObjectIdUtil.id()).build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }
    
    @PutMapping(value="/{companyId}")
    public ResponseEntity<String> companyUpdate(@PathVariable String companyId,@RequestBody CompanyUpdateCmd request) {
        final CompanyUpdateCmd cmd = CompanyUpdateCmd.of(request).id(companyId).build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));
    }
    
    @DeleteMapping(value="/{companyId}")
    public ResponseEntity<String> companyRemove(@PathVariable String companyId){
        final CompanyDeleteCmd cmd =CompanyDeleteCmd.builder().id(companyId).build();
        return ResponseEntity.accepted().body(gateway.sendAndWait(cmd));       
    }
}
