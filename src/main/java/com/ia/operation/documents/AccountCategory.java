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
        /*@formatter:off*/
        return AccountCategory.builder()
                .description(event.getDescription())
                .id(event.getId());
        /*@formatter:on*/
    }
    
    public static AccountCategoryBuilder from(AccountCategoryUpdatedEvent event) {
        /*@formatter:off*/
        return AccountCategory.builder()
                .description(event.getDescription())
                .id(event.getId());
        /*@formatter:on*/
    }

}
