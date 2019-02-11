package com.ia.operation.documents;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.events.created.OperationCategoryCreatedEvent;
import com.ia.operation.events.updated.OperationCategoryUpdatedEvent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document
public class OperationCategory {
    private String id;
    private String description;

    public static OperationCategoryBuilder from(OperationCategoryCreatedEvent event) {
        return OperationCategory.builder()
                .description(event.getDescription())
                .id(event.getId());
    }
    
    public static OperationCategoryBuilder from(OperationCategoryUpdatedEvent event) {
        return OperationCategory.builder()
                .description(event.getDescription())
                .id(event.getId());
    }

}
