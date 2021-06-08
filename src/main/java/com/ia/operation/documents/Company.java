package com.ia.operation.documents;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ia.operation.events.created.CompanyCreatedEvent;
import com.ia.operation.events.updated.CompanyUpdatedEvent;

import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
@JsonInclude(value = Include.NON_EMPTY)
public class Company {
    @Id
    private String id;
    private String description;
    private Map<String, Object> details;

    public static Company of(CompanyCreatedEvent event) {
        return Company.builder().id(event.getId()).description(event.getDescription()).details(event.getDetails()).build();
    }

    public static Company of(CompanyUpdatedEvent event) {
        return Company.builder().id(event.getId()).description(event.getDescription()).details(event.getDetails()).build();
    }
}
