package com.ia.operation.commands.creation;

import com.ia.operation.events.created.OperationCategoryCreatedEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OperationCategoryCreationCmd {
    private String id;
    private String description;
    
    public static OperationCategoryCreatedEvent.OperationCategoryCreatedEventBuilder from (OperationCategoryCreationCmd cmd){
        return OperationCategoryCreatedEvent.builder()
            .id(cmd.getId())
            .description(cmd.getDescription());
    }
    
    public static OperationCategoryCreationCmdBuilder of (OperationCategoryCreationCmd cmd){
        return OperationCategoryCreationCmd.builder()
            .id(cmd.getId())
            .description(cmd.getDescription());
    }

}
