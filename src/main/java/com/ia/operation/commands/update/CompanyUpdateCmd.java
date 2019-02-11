package com.ia.operation.commands.update;

import java.util.Map;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.ia.operation.events.updated.CompanyUpdatedEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompanyUpdateCmd {
    @TargetAggregateIdentifier
    private String id;
    private String description;
    private Map<String, Object> details;
    
    public static CompanyUpdateCmdBuilder of(CompanyUpdateCmd cmd) {
        return CompanyUpdateCmd.builder()
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .id(cmd.getId());
    }
    
    public static CompanyUpdatedEvent.CompanyUpdatedEventBuilder from(CompanyUpdateCmd cmd) {
        return CompanyUpdatedEvent.builder()
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .id(cmd.getId());
    }

}
