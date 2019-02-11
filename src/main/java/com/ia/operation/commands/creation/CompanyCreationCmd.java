package com.ia.operation.commands.creation;

import java.util.Map;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.ia.operation.commands.update.CompanyUpdateCmd;
import com.ia.operation.events.created.CompanyCreatedEvent;
import com.ia.operation.events.created.CompanyCreatedEvent.CompanyCreatedEventBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompanyCreationCmd {
    @TargetAggregateIdentifier
    private String id;
    private String description;
    private Map<String, Object> details;
    
    public static CompanyCreatedEventBuilder from(CompanyCreationCmd cmd) {
        return CompanyCreatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails());
    }
    public static CompanyCreationCmdBuilder of(CompanyCreationCmd cmd) {
        return CompanyCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails());
    }
    
    public static CompanyCreationCmdBuilder of(CompanyUpdateCmd cmd) {
        return CompanyCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails());
    }
}
