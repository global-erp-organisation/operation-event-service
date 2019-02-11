package com.ia.operation.commands.update;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.ia.operation.events.updated.OperationCategoryUpdatedEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OperationCategoryUpdateCmd {
    @TargetAggregateIdentifier
    private String id;
    private String description;
    
    public static OperationCategoryUpdatedEvent.OperationCategoryUpdatedEventBuilder from (OperationCategoryUpdateCmd cmd){
        return OperationCategoryUpdatedEvent.builder()
            .id(cmd.getId())
            .description(cmd.getDescription());
    }
    
    public static OperationCategoryUpdateCmdBuilder of (OperationCategoryUpdateCmd cmd){
        return OperationCategoryUpdateCmd.builder()
            .id(cmd.getId())
            .description(cmd.getDescription());
    }

}
