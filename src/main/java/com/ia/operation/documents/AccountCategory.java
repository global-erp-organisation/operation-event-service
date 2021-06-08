package com.ia.operation.documents;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.events.created.AccountCategoryCreatedEvent;
import com.ia.operation.events.updated.AccountCategoryUpdatedEvent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document
public class AccountCategory {
    private String id;
    private String description;

    public static AccountCategoryBuilder from(AccountCategoryCreatedEvent event) {
        return AccountCategory.builder().description(event.getDescription()).id(event.getId());
    }
    
    public static AccountCategoryBuilder from(AccountCategoryUpdatedEvent event) {
        return AccountCategory.builder().description(event.getDescription()).id(event.getId());
    }

}
